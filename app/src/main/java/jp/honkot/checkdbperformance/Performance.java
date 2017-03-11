package jp.honkot.checkdbperformance;

/**
 * Created by hiroki on 2017-03-10.
 */

public class Performance {
    private long totalScore;
    private int count;

    private long tmpStart;
    private long tmpFinish;
    private long tmpResult;

    private String expandInfo;

    public void measureStart() {
        tmpResult = 0;
        tmpStart = System.currentTimeMillis();
        tmpFinish = 0;
    }

    public long measureFinish() {
        if (tmpResult == 0) {
            tmpFinish = System.currentTimeMillis();
            tmpResult = tmpFinish - tmpStart;
            totalScore += tmpResult;
            count++;
        }
        return tmpResult;
    }

    public long getMesureResult() {
        return tmpResult;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public double getAverageScore() {
        if (count != 0) {
            return (double)totalScore / (double)count;
        } else {
            return 0.0d;
        }
    }

    public void setExpandInfo(String info) {
        expandInfo = info;
    }

    public String getDisplayResult(String tag) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag == null ? "Performance" : tag);
        if (expandInfo != null) {
            sb.append(" (").append(expandInfo).append(")");
        }
        sb.append("\n - total: ").append(getTotalScore()).append("ms");
        if (count > 1) {
            sb.append(", ave: ").append(getAverageScore()).append("ms ");
        }
        return sb.toString();
    }
}
