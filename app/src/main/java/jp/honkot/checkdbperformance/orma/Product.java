package jp.honkot.checkdbperformance.orma;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.Getter;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

@Table("Orma_Product")
public class Product {

    @Column(value = BaseColumns._ID)
    @PrimaryKey(autoincrement = true)
    private long id;

    @NonNull
    @Column
    private String name;

    @Column(indexed = true)
    private int price;

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
    public String getName() {
        return name;
    }

    @Setter
    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Getter
    public int getPrice() {
        return price;
    }

    @Setter
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
}
