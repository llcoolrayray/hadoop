package org.example.temperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author Wang.Rui.Barney
 */
public class MaxTemperature {
    public static void main(String[] args) throws Exception {
        // 创建本次mr程序的job实例
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //设置本次 job 的名称
        job.setJobName("test");

        // 指定本次 job 运行的主类
        job.setJarByClass(MaxTemperature.class);

        // 指定本次 job 的具体 mapper reducer 实现类
        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);

        // 指定本次 job map 阶段的输出数据类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 指定本次 job reduce 阶段的输出数据类型 也就是整个 mr 任务的最终输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 指定本次 job 待处理数据的目录和程序执行完输出结果存放的目录
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 提交本次 job
        boolean b = job.waitForCompletion(true);

        System.exit(b ? 0 : 1);
    }
}
