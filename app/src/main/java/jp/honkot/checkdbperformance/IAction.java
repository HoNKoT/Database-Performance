package jp.honkot.checkdbperformance;

public interface IAction {
    Performance insert();
    Performance insertBulk();
    Performance update();
    Performance delete();
    Performance sumQtyBySimple();
    Performance sumQtyByFaster();
    Performance initialize();
}
