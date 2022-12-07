package org.example.WordCount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author Wang.Rui.Barney
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    private final Logger logger = Logger.getLogger(WordCountMapper.class);
    private final static LongWritable VALUE_OUT = new LongWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context) throws IOException, InterruptedException {
        String[] words = value.toString().split(" ");
        logger.info(words);
        for (String word : words) {
            context.write(new Text(word), VALUE_OUT);
        }
    }
}
