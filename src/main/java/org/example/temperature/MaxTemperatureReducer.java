package org.example.temperature;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @author Wang.Rui.Barney
 */
public class MaxTemperatureReducer extends  Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        System.out.println("--->Reducer-->" + Thread.currentThread().getName());
        int sum = 0;
        for (IntWritable i : values) {
            sum = sum + i.get();
        }
        context.write(key, new IntWritable(sum));
    }
}
