package org.example.WordCount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author Wang.Rui.Barney
 */
public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
    private final Logger logger = Logger.getLogger(WordCountReducer.class);

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
        long count = 0;
        for (LongWritable longWritable : values) {
            count += longWritable.get();
        }

        logger.info(key+":"+count);
        context.write(key, new LongWritable(count));
    }
}
