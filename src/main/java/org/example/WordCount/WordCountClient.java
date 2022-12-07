package org.example.WordCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author Wang.Rui.Barney
 */
public class WordCountClient extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        // 默认为本地模式运行 MapReduce 程序，因此需要代码或配置文件设置为 yarn 集群模式（或者服务器上配置文件上配置好）
        configuration.set("mapreduce.framework.name", "yarn");
        int status = ToolRunner.run(configuration, new WordCountClient(), args);
        System.exit(status);
    }

    @Override
    public int run(String[] args) throws Exception {
        // 创建本次 mr 程序的 job 实例
        Job job = Job.getInstance(getConf(), WordCountClient.class.getSimpleName());

        // 指定本次 job 运行的主类
        job.setJarByClass(WordCountClient.class);

        // 指定本次 job 的具体 mapper reducer 实现类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 指定本次 job map 阶段的输出数据类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        // 指定本次 job reduce 阶段的输出数据类型。也就是整个 mr 任务的最终输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 指定本次 job 待处理数据的目录和程序执行完输出结果存放的目录
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 提交本次 job
        return job.waitForCompletion(true) ? 0 : 1;
    }
}
