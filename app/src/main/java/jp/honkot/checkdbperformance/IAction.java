package jp.honkot.checkdbperformance;

public interface IAction {
    Performance insert();
    Performance insertBulk();
    Performance sumQtyBySimple();
    Performance sumQtyByFaster();
    Performance initialize();
}
