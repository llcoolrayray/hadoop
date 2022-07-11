package org.example.temperature;
import com.avocent.mtp.commons.json.JsonUtil;
import com.avocent.taf.plugin.engineadapter.rdua.model.HourData;
import com.avocent.taf.plugin.engineadapter.rdua.service.AlarmService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.avocent.mtp.model.EventProperties.TIMESTAMP;
import static com.avocent.taf.plugin.engineadapter.rdua.constants.ConfigurationInstanceProperties.*;

public class HourMaxMinValue {
    private static final Logger LOGGER = LoggerFactory.getLogger(HourMaxMinValue.class);

    private float maxValue;
    private long maxTimestamp;
    private float minValue;
    private long minTimestamp;
    private BigDecimal total;
    private long count;
    private List<HourData> hourDataList = new ArrayList<>();

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
        this.total = new BigDecimal(minValue);
        hourDataList.add(new HourData(convertToUtcString(minTimestamp),count,total,minValue,total.floatValue()/count));
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

    private void updateTotalValue(long timestamp , float value) {
        this.total = this.total.add(new BigDecimal(value));
        this.count++;
        hourDataList.add(new HourData(convertToUtcString(timestamp),count,total,value,total.floatValue()/count));
    }

    public synchronized void updateTagData(long timestamp, float value) {
        this.setMaxValueIfNeed(timestamp, value);
        this.setMinValueIfNeed(timestamp, value);
        this.updateTotalValue(timestamp,value);
    }

    public JsonNode toJsonNode() {
        ObjectNode node = JsonUtil.createObjectNode();
        node.with(MAX).put(TIMESTAMP, convertToUtcString(maxTimestamp));
        node.with(MAX).put(VALUE, maxValue);
        node.with(MIN).put(TIMESTAMP, convertToUtcString(minTimestamp));
        node.with(MIN).put(VALUE, minValue);
        node.with(AVG).put(VALUE, this.total.floatValue()/this.count);
        node.with(AVG).put(COUNT, String.valueOf(count));
        return node;
    }

    private String convertToUtcString(long timestamp) {
        return new DateTime(timestamp, DateTimeZone.UTC).toString();
    }
}
