package org.example.covid;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;
import org.example.pojo.CovidBean;

import java.util.HashMap;

/**
 * 自定义分区器。只要 getPartition 返回的值一样就会被分到同一区。
 * 所谓的同一个分区指的是 map 处理过的数据到同一个 ReduceTask 中
 *
 * @author Wang.Rui.Barney
 */
public class ProvincePartitioner extends Partitioner<CovidBean, NullWritable> {

    public static HashMap<String, Integer> map = new HashMap<>();

    static {
        map.put("上海市", 0);
        map.put("云南省", 1);
        map.put("内蒙古自治区", 2);
        map.put("北京市", 3);
    }

    @Override
    public int getPartition(CovidBean covidBean, NullWritable nullWritable, int numPartitions) {
        Integer code = map.get(covidBean.getProvince());
        if (null != code) {
            return code;
        }

        return 4;
    }
}
