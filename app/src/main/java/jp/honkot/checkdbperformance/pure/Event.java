package jp.honkot.checkdbperformance.pure;

import android.database.Cursor;
import android.support.annotation.NonNull;

public class Event {

    /**
     * Primary key
     */
    private long id;

    @NonNull
    private String action;

    private int quantity;

    private int status;

    public int c1;

    public int c2;

    public int c3;

    public int c4;

    public int c5;

    public int c6;

    public int c7;

    public int c8;

    public int c9;

    public int c10;

    private long product;

    public Event() {}

    public Event(Cursor cr) {
        setId(cr.getLong(Const.INDEX_ID));
        setAction(cr.getString(Const.INDEX_ACTION));
        setQuantity(cr.getInt(Const.INDEX_QUANTITY));
        setStatus(cr.getInt(Const.INDEX_STATUS));
        c1 = cr.getInt(Const.INDEX_C1);
        c2 = cr.getInt(Const.INDEX_C2);
        c3 = cr.getInt(Const.INDEX_C3);
        c4 = cr.getInt(Const.INDEX_C4);
        c5 = cr.getInt(Const.INDEX_C5);
        c6 = cr.getInt(Const.INDEX_C6);
        c7 = cr.getInt(Const.INDEX_C7);
        c8 = cr.getInt(Const.INDEX_C8);
        c9 = cr.getInt(Const.INDEX_C9);
        c10 = cr.getInt(Const.INDEX_C10);
        setProduct(cr.getLong(Const.INDEX_PRODUCT));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getAction() {
        return action;
    }

    public void setAction(@NonNull String action) {
        this.action = action;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NonNull
    public int getStatus() {
        return status;
    }

    public void setStatus(@NonNull int status) {
        this.status = status;
    }

    public void setC(int value) {
        c1 = value;
        c2 = value;
        c3 = value;
        c4 = value;
        c5 = value;
        c6 = value;
        c7 = value;
        c8 = value;
        c9 = value;
        c10 = value;
    }

    public long getProduct() {
        return product;
    }

    public void setProduct(long product) {
        this.product = product;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("id=").append(id);
        sb.append(", quantity=").append(quantity);
        sb.append(", action='").append(action).append('\'');
        sb.append(", status=").append(status);
        sb.append(", product=").append(product);
        sb.append('}');
        return sb.toString();
    }

    public class Const {
        public static final String TABLE_NAME = "Pure_Event";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_ACTION = "action";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_C1 = "c1";
        public static final String COLUMN_C2 = "c2";
        public static final String COLUMN_C3 = "c3";
        public static final String COLUMN_C4 = "c4";
        public static final String COLUMN_C5 = "c5";
        public static final String COLUMN_C6 = "c6";
        public static final String COLUMN_C7 = "c7";
        public static final String COLUMN_C8 = "c8";
        public static final String COLUMN_C9 = "c9";
        public static final String COLUMN_C10 = "c10";
        public static final String COLUMN_PRODUCT = "product";

        public static final int INDEX_ID = 0;
        public static final int INDEX_QUANTITY = 1;
        public static final int INDEX_ACTION = 2;
        public static final int INDEX_STATUS = 3;
        public static final int INDEX_C1 = 4;
        public static final int INDEX_C2 = 5;
        public static final int INDEX_C3 = 6;
        public static final int INDEX_C4 = 7;
        public static final int INDEX_C5 = 8;
        public static final int INDEX_C6 = 9;
        public static final int INDEX_C7 = 10;
        public static final int INDEX_C8 = 11;
        public static final int INDEX_C9 = 12;
        public static final int INDEX_C10 = 13;
        public static final int INDEX_PRODUCT = 14;
    }

    public static final String[] COLUMNS = {
            Const.COLUMN_ID,
            Const.COLUMN_QUANTITY,
            Const.COLUMN_ACTION,
            Const.COLUMN_STATUS,
            Const.COLUMN_C1,
            Const.COLUMN_C2,
            Const.COLUMN_C3,
            Const.COLUMN_C4,
            Const.COLUMN_C5,
            Const.COLUMN_C6,
            Const.COLUMN_C7,
            Const.COLUMN_C8,
            Const.COLUMN_C9,
            Const.COLUMN_C10,
            Const.COLUMN_PRODUCT,
    };
}
