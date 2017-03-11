package jp.honkot.checkdbperformance.pure;

import android.database.Cursor;
import android.support.annotation.NonNull;

public class Product {

    private long id;

    private String name;

    private int price;

    public Product(Cursor cr) {
        setId(cr.getLong(Const.INDEX_ID));
        setName(cr.getString(Const.INDEX_NAME));
        setPrice(cr.getInt(Const.INDEX_PRICE));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }

    public class Const {
        public static final String TABLE_NAME = "Pure_Product";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";

        public static final int INDEX_ID = 0;
        public static final int INDEX_NAME = 1;
        public static final int INDEX_PRICE = 2;
    }

    public static final String[] COLUMNS = {
            Const.COLUMN_ID,
            Const.COLUMN_NAME,
            Const.COLUMN_PRICE,
    };
}
