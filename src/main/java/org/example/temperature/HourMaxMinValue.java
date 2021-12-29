package org.example.temperature;

public class HourMaxMinValue {
    private float maxValue;
    private long maxTimestamp;
    private float minValue;
    private long minTimestamp;
    private float total;
    private long count;

    public float getMaxValue() {
        return maxValue;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public HourMaxMinValue(long maxTimestamp, float maxValue, long minTimestamp, float minValue) {
        this.maxValue = maxValue;
        this.maxTimestamp = maxTimestamp;
        this.minValue = minValue;
        this.minTimestamp = minTimestamp;
        this.count = 1;
        this.total = minValue;
    }

    public HourMaxMinValue(long initDateTime, float initValue) {
        this(initDateTime, initValue, initDateTime, initValue);
    }

    private void setMaxValueIfNeed(long timestamp, float value) {
        if (value > maxValue) {
            this.maxTimestamp = timestamp;
            this.maxValue = value;
        }
    }

    private void setMinValueIfNeed(long timestamp, float value) {
        if (value < minValue) {
            this.minTimestamp = timestamp;
            this.minValue = value;
        }
    }

    private void updateTotalValue(float value) {
        this.total = this.total + value;
        this.count++;
    }

    public synchronized void updateTagData(long timestamp, float value) {
        this.setMaxValueIfNeed(timestamp, value);
        this.setMinValueIfNeed(timestamp, value);
        this.updateTotalValue(value);
    }

    public static void main(String[] args) {
        // 1 , 3 , 5
        int timestamp = 666;
        HourMaxMinValue hourMaxMinValue = new HourMaxMinValue(timestamp, 1, timestamp, 1);
        hourMaxMinValue.updateTagData(timestamp, 3);
        hourMaxMinValue.updateTagData(timestamp, 5);

        System.out.println(hourMaxMinValue);
    }
}
