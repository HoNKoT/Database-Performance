package jp.honkot.checkdbperformance;

import java.util.Random;

public abstract class DaoBase {

    public static final int PRODUCT_NUM = 100;
    public static final int ACTION_NUM = 3;
    public static final int QTY_MAX = 100;
    public static final int TEST_COUNT = 10000;
    public Random random = new Random();

    public abstract void initProduct();
}
