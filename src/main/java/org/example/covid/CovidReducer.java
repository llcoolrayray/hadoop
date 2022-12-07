package org.example.covid;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.example.pojo.CovidBean;

import java.io.IOException;

/**
 * @author Wang.Rui.Barney
 */
public class CovidReducer extends Reducer<CovidBean, LongWritable, CovidBean, LongWritable> {

    @Override
    protected void reduce(CovidBean key, Iterable<LongWritable> values, Reducer<CovidBean, LongWritable, CovidBean, LongWritable>.Context context) throws IOException, InterruptedException {
        int i = 0;
        for (LongWritable longWritable : values) {
            if (i < 3) {
                context.write(key, longWritable);
                i++;
            }else {
                return;
            }
        }
    }
}