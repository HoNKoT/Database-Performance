package jp.honkot.checkdbperformance.orma;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.github.gfx.android.orma.SingleAssociation;
import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.Getter;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

@Table("Orma_Event")
public class Event {

    @Column(value = BaseColumns._ID)
    @PrimaryKey(autoincrement = true)
    private long id;

    @NonNull
    @Column
    private String action;

    @Column
    private int quantity;

    @Column
    private int status;

    @Column
    public int c1;

    @Column
    public int c2;

    @Column
    public int c3;

    @Column
    public int c4;

    @Column
    public int c5;

    @Column
    public int c6;

    @Column
    public int c7;

    @Column
    public int c8;

    @Column
    public int c9;

    @Column
    public int c10;

    @NonNull
    @Column(indexed = true)
    private SingleAssociation<Product> product;

    @Getter
    public long getId() {
        return id;
    }

    @Setter
    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    @Getter
    public String getAction() {
        return action;
    }

    @Setter
    public void setAction(@NonNull String action) {
        this.action = action;
    }

    @Getter
    public int getQuantity() {
        return quantity;
    }

    @Setter
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NonNull
    @Getter
    public int getStatus() {
        return status;
    }

    @Setter
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

    @NonNull
    @Getter
    public SingleAssociation<Product> getProduct() {
        return product;
    }

    @Setter
    public void setProduct(@NonNull SingleAssociation<Product> product) {
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
}
