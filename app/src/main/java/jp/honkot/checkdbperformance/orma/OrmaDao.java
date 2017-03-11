package jp.honkot.checkdbperformance.orma;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.github.gfx.android.orma.AccessThreadConstraint;
import com.github.gfx.android.orma.Inserter;
import com.github.gfx.android.orma.SingleAssociation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.honkot.checkdbperformance.IAction;
import jp.honkot.checkdbperformance.Performance;

public class OrmaDao implements IAction {

    private static final String DATABASE_NAME = "orma";
    private static final int PRODUCT_NUM = 100;
    private static final int ACTION_NUM = 3;
    private static final int QTY_MAX = 100;
    private static final int TEST_COUNT = 10000;
    private static OrmaDatabase orma;
    private Random random = new Random();

    public OrmaDao(Context context) {
        if (orma == null) {
            orma = OrmaDatabase.builder(context)
                    .readOnMainThread(AccessThreadConstraint.NONE)
                    .writeOnMainThread(AccessThreadConstraint.NONE)
                    .name(DATABASE_NAME)
                    .trace(false)
                    .build();
        }
    }

    private Event generateNewEvent() {
        int action = random.nextInt(ACTION_NUM);
        int productIndex = random.nextInt(PRODUCT_NUM);
        int qty = random.nextInt(QTY_MAX);
        Event event = new Event();
        event.setAction("Action #" + action);
        event.setQuantity(qty);
        event.setProduct(SingleAssociation.just(
                orma.relationOfProduct().selector().idEq(productIndex + 1).value()));
        event.setC(random.nextInt());

        return event;
    }

    public void initProduct() {
        if (orma.relationOfProduct().selector().isEmpty()) {
            Random random = new Random();
            ArrayList<Product> insertItem = new ArrayList<>();
            for (int i = 0; i < PRODUCT_NUM; i++) {
                Product product = new Product();
                product.setName("Product #" + i);
                product.setPrice(random.nextInt(500) + 300);
                insertItem.add(product);
            }

            Performance performance = new Performance();
            performance.measureStart();
            Inserter<Product> productInserter = orma.prepareInsertIntoProduct();
            productInserter.executeAll(insertItem);
            performance.measureFinish();

            Log.i(getClass().getSimpleName(), performance.getDisplayResult("Orma init product"));
        }
    }

    @Override
    public Performance insert() {
        Performance performance = new Performance();
        Inserter<Event> inserter = orma.prepareInsertIntoEvent();

        for (int i = 0; i < TEST_COUNT; i++) {
            // make event
            Event event = generateNewEvent();

            // insert one by one
            performance.measureStart();
            inserter.execute(event);
            performance.measureFinish();
        }
        return performance;
    }

    @Override
    public Performance insertBulk() {
        Performance performance = new Performance();
        Inserter<Event> inserter = orma.prepareInsertIntoEvent();

        ArrayList<Event> insertItems = new ArrayList<>();
        for (int i = 0; i < TEST_COUNT; i++) {
            // make event
            Event event = generateNewEvent();

            // stock it
            insertItems.add(event);
        }

        // insert them with bulk insert
        performance.measureStart();
        inserter.executeAll(insertItems);
        performance.measureFinish();
        return performance;
    }

    @Override
    public Performance sumQtyBySimple() {

        Performance performance = new Performance();
        performance.measureStart();

        // faze 1. get product
        List<Product> products = orma.relationOfProduct()
                .selector()
                .priceGt(500)
                .toList();

        long sum = 0;
        for (Product product : products) {
            // faze 2. get related events
            List<Event> events = orma.relationOfEvent()
                    .selector()
                    .productEq(product)
                    .toList();

            // faze 3. make sum for quantity
            for (Event event : events) {
                sum += event.getQuantity();
            }
        }

        performance.measureFinish();
        performance.setExpandInfo(
                orma.relationOfEvent().count() + "records " +
                        ", product " + products.size() +
                        ", sum " + sum);

        return performance;
    }

    @Override
    public Performance sumQtyByFaster() {
        Performance performance = new Performance();
        performance.measureStart();

        // faze 1. get product
        Cursor productsCursor = orma.relationOfProduct()
                .selector()
                .priceGt(500)
                .executeWithColumns(Product_Schema.INSTANCE.id.getEscapedName());

        // faze 2. make IN clauses
        //  -> ('_id' IN (1, 2, 3, .... ))
        StringBuilder productsInClauses = new StringBuilder()
                .append(Event_Schema.INSTANCE.product.getEscapedName())
                .append(" IN (");
        int index = 0;
        while (productsCursor.moveToNext()) {
            productsInClauses.append(productsCursor.getLong(0));
            if (index < productsCursor.getCount() - 1) {
                index++;
                productsInClauses.append(", ");
            }
        }
        productsInClauses.append(")");
        int productCountForDebug = productsCursor.getCount();
        productsCursor.close();

        // faze 3. make sum for quantity
        //  -> select sum('quantity') where '_id' IN (1, 2, 3, .... )
        long sum = orma.relationOfEvent()
                .selector()
                .where(productsInClauses.toString(), new ArrayList<>())
                .sumByQuantity();

        performance.measureFinish();
        performance.setExpandInfo(
                orma.relationOfEvent().count() + "records " +
                        ", product " + productCountForDebug +
                        ", sum " + sum);

        return performance;
    }


    @Override
    public Performance initialize() {
        Performance performance = new Performance();
        performance.setExpandInfo(orma.relationOfEvent().count() + " records");

        performance.measureStart();
        orma.relationOfEvent().deleter().execute();
        performance.measureFinish();

        return performance;
    }
}
