package jp.honkot.checkdbperformance.pure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.honkot.checkdbperformance.DaoBase;
import jp.honkot.checkdbperformance.IAction;
import jp.honkot.checkdbperformance.Performance;

/**
 * Created by hiroki on 2017-03-10.
 */

public class PureAndroidDao extends DaoBase implements IAction {

    private OpenHelper mSQLHelper;
    private SQLiteDatabase mDB;
    private Context mContext;
    private Random random = new Random();

    public PureAndroidDao(Context con) {
        mContext = con;
        mSQLHelper = new OpenHelper(mContext);
        mDB =  mSQLHelper.getWritableDatabase();
    }

    /**
     * Create new Event, the quantity and related product id are decided at random.
     * @return new Event
     */
    private Event generateNewEvent() {
        int action = random.nextInt(ACTION_NUM);
        int productIndex = random.nextInt(PRODUCT_NUM);
        int qty = random.nextInt(QTY_MAX);
        Event event = new Event();
        event.setAction("Action #" + action);
        event.setQuantity(qty);
        event.setProduct(productIndex + 1);
        event.setC(random.nextInt());

        return event;
    }

    public void initProduct() {
        Cursor allProducts = mDB.query(Product.Const.TABLE_NAME,
                Product.COLUMNS,
                null, null, null, null, null);

        if (!allProducts.moveToFirst()) {
            Performance performance = new Performance();
            performance.measureStart();
            for (int i = 0; i < PRODUCT_NUM; i++) {
                ContentValues cv = new ContentValues();
                cv.put(Product.Const.COLUMN_NAME, "Product #" + i);
                cv.put(Product.Const.COLUMN_PRICE, random.nextInt(500) + 300);
                mDB.insert(Product.Const.TABLE_NAME, null, cv);
            }
            performance.measureFinish();

            Log.i(getClass().getSimpleName(), performance.getDisplayResult("Pure android init product"));

        }
        allProducts.close();
    }

    @Override
    public Performance insert() {
        Performance performance = new Performance();

        for (int i = 0; i < TEST_COUNT; i++) {
            // make event
            Event event = generateNewEvent();

            ContentValues cv = new ContentValues();
            cv.put(Event.Const.COLUMN_ACTION, event.getAction());
            cv.put(Event.Const.COLUMN_STATUS, event.getStatus());
            cv.put(Event.Const.COLUMN_QUANTITY, event.getQuantity());
            cv.put(Event.Const.COLUMN_C1, event.c1);
            cv.put(Event.Const.COLUMN_C2, event.c2);
            cv.put(Event.Const.COLUMN_C3, event.c3);
            cv.put(Event.Const.COLUMN_C4, event.c4);
            cv.put(Event.Const.COLUMN_C5, event.c5);
            cv.put(Event.Const.COLUMN_C6, event.c6);
            cv.put(Event.Const.COLUMN_C7, event.c7);
            cv.put(Event.Const.COLUMN_C8, event.c8);
            cv.put(Event.Const.COLUMN_C9, event.c9);
            cv.put(Event.Const.COLUMN_C10, event.c10);
            cv.put(Event.Const.COLUMN_PRODUCT, event.getProduct());

            // insert one by one
            performance.measureStart();
            mDB.insert(Event.Const.TABLE_NAME, null, cv);
            performance.measureFinish();
        }
        return performance;
    }

    @Override
    public Performance insertBulk() {
        return insert();
    }

    @Override
    public Performance update() {
        Performance performance = new Performance();

        // faze 1. get all Event records (only id)
        Cursor eventIdsCursor = mDB.query(
                Event.Const.TABLE_NAME,
                new String[] {Event.Const.COLUMN_ID},
                null, null, null, null, null);

        while (eventIdsCursor.moveToNext()) {
            // faze 2. generate new product Id
            long newProductId = random.nextInt(PRODUCT_NUM) + 1;

            // faze 3. update product id to it one by one
            ContentValues cv = new ContentValues();
            cv.put(Event.Const.COLUMN_PRODUCT, newProductId);

            performance.measureStart();
            mDB.update(
                    Event.Const.TABLE_NAME,
                    cv,
                    Event.Const.COLUMN_ID + " = " + eventIdsCursor.getLong(0),
                    null);
            performance.measureFinish();
        }
        eventIdsCursor.close();

        return performance;
    }

    @Override
    public Performance delete() {
        Performance performance = new Performance();

        // faze 1. get all Event records (only id)
        Cursor eventIdsCursor = mDB.query(
                Event.Const.TABLE_NAME,
                new String[] {Event.Const.COLUMN_ID},
                null, null, null, null,
                Event.Const.COLUMN_ID + " ASC");

        // faze 2. delete one by one (10000 records only)
        int count = 0;
        while (eventIdsCursor.moveToNext()) {
            performance.measureStart();
            mDB.delete(
                    Event.Const.TABLE_NAME,
                    Event.Const.COLUMN_ID + " = " + eventIdsCursor.getLong(0),
                    null);
            performance.measureFinish();

            count++;
            if (count >= TEST_COUNT) {
                break;
            }
        }
        eventIdsCursor.close();

        return performance;
    }

    @Override
    public Performance sumQtyBySimple() {

        Performance performance = new Performance();
        performance.measureStart();

        // faze 1. get product
        List<Product> products = findProductsHt500();

        long sum = 0;
        int eventCount = 0;
        for (Product product : products) {
            // faze 2. get related events
            List<Event> events = findEventsWith(product);

            // faze 3. make sum for quantity
            for (Event event : events) {
                sum += event.getQuantity();
            }
            eventCount += events.size();
        }

        performance.measureFinish();
        performance.setExpandInfo(
                allEventCount() + "records " +
                        ", product " + products.size() +
                        ", sum " + sum + " (related " + eventCount + " Events)");

        return performance;
    }

    @Override
    public Performance sumQtyByFaster() {
        Performance performance = new Performance();
        performance.measureStart();

        // faze 1. get product
        Cursor productsCursor = mDB.query(
                Product.Const.TABLE_NAME,
                Product.COLUMNS,
                Product.Const.COLUMN_PRICE + " >= " + 500,
                null, null, null, null);

        // faze 2. make IN clauses
        //  -> ('_id' IN (1, 2, 3, .... ))
        StringBuilder productsInClauses = new StringBuilder()
                .append("`")
                .append(Event.Const.COLUMN_PRODUCT)
                .append("` IN (");
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
        Cursor sumCursor = mDB.rawQuery(
                "SELECT sum(`" + Event.Const.COLUMN_QUANTITY + "`) FROM " + Event.Const.TABLE_NAME
                + " WHERE " + productsInClauses.toString(), null);
        sumCursor.moveToFirst();
        long sum = sumCursor.getInt(0);
        sumCursor.close();

        performance.measureFinish();
        Cursor countCorsorForDebug = mDB.rawQuery(
                "SELECT " + Event.Const.COLUMN_ID + " FROM " + Event.Const.TABLE_NAME
                        + " WHERE " + productsInClauses.toString(), null);
        int eventCount = countCorsorForDebug.getCount();
        countCorsorForDebug.close();
        performance.setExpandInfo(
                allEventCount() + "records " +
                        ", product " + productCountForDebug +
                        ", sum " + sum + " (related " + eventCount + " Events)");

        return performance;
    }

    public int allEventCount() {
        Cursor eventsCursor = mDB.query(
                Event.Const.TABLE_NAME,
                Event.COLUMNS,
                null, null, null, null, null);
        int count = eventsCursor.getCount();
        eventsCursor.close();

        return count;
    }

    private List<Product> findProductsHt500() {
        ArrayList<Product> products = new ArrayList<>();

        Cursor productsCursor = mDB.query(
                Product.Const.TABLE_NAME,
                Product.COLUMNS,
                Product.Const.COLUMN_PRICE + " >= " + 500,
                null, null, null, null);
        while (productsCursor.moveToNext()) {
            Product product = new Product(productsCursor);
            products.add(product);
        }
        productsCursor.close();

        return products;
    }

    private List<Event> findEventsWith(Product product) {
        ArrayList<Event> events = new ArrayList<>();

        Cursor eventsCursor = mDB.query(
                Event.Const.TABLE_NAME,
                Event.COLUMNS,
                Event.Const.COLUMN_PRODUCT + " = " + product.getId(),
                null, null, null, null);
        while (eventsCursor.moveToNext()) {
            Event event = new Event(eventsCursor);
            events.add(event);
        }
        eventsCursor.close();

        return events;
    }

    @Override
    public Performance initialize() {
        Performance performance = new Performance();
        performance.setExpandInfo(allEventCount() + " records");

        performance.measureStart();
        mSQLHelper.recreateEventTable(mDB);
        performance.measureFinish();

        return performance;
    }

    @Override
    protected void finalize() throws Throwable {
        if (mDB != null && mDB.isOpen()) mDB.close();
        if (mSQLHelper != null) mSQLHelper.close();
        super.finalize();
    }
}
