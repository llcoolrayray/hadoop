package org.example.covid;

import com.mysql.jdbc.Buffer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.example.pojo.Student;

import java.io.IOException;

/**
 * @author Wang.Rui.Barney
 */
public class CovidMapper extends Mapper<LongWritable, Student, LongWritable, Student> {

    @Override
    protected void map(LongWritable key, Student value, Mapper<LongWritable, Student, LongWritable, Student>.Context context) throws IOException, InterruptedException {
        System.out.println(Buffer.class);
        context.write(key, value);
    }
}
