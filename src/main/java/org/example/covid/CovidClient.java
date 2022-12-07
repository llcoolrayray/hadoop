package org.example.covid;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.example.pojo.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Wang.Rui.Barney
 */
public class CovidClient extends Configured implements Tool {
    private static Logger logger = LoggerFactory.getLogger(CovidClient.class);

    public static void main(String[] args) throws Exception {
        logger.info("start mother fucker");
        Configuration configuration = new Configuration();

        // 默认为本地模式运行 MapReduce 程序，因此需要代码或配置文件设置为 yarn 集群模式（或者服务器上配置文件上配置好）
        configuration.set("mapreduce.framework.name", "yarn");
        DBConfiguration.configureDB(
                configuration,
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://172.18.216.8:3306/mtp",
                "root",
                "root"
        );

        int status = ToolRunner.run(configuration, new CovidClient(), args);
        System.exit(status);
    }

    @Override
    public int run(String[] args) throws Exception {
        // 创建本次 mr 程序的 job 实例
        Job job = Job.getInstance(getConf(), CovidClient.class.getSimpleName());

        //job.addFileToClassPath(new Path("/libs/mysql-connector-java-8.0.28.jar"));

        // 指定本次 job 运行的主类
        job.setJarByClass(CovidClient.class);

        // 指定本次 job 的具体 mapper reducer 实现类
        job.setMapperClass(CovidMapper.class);
        job.setReducerClass(CovidReducer.class);

        // 指定本次 job map 阶段的输出数据类型
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Student.class);

        // 指定本次 job reduce 阶段的输出数据类型。也就是整个 mr 任务的最终输出类型
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Student.class);

        // 设置分片规则
        job.setNumReduceTasks(0);
        //job.setPartitionerClass(ProvincePartitioner.class);

        // 设置分组规则
        //job.setGroupingComparatorClass(CovidGroupComparator.class);

        // 设置输入组件
        // 添加读取数据相关参数
        DBInputFormat.setInput(
                job,
                Student.class,
                "select * from student",
                "select count(id) from student"
        );

        job.setInputFormatClass(DBInputFormat.class);

        // 指定本次 job 待处理数据的目录和程序执行完输出结果存放的目录
        //FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path("/test/output-file"));

        // 提交本次 job
        return job.waitForCompletion(true) ? 0 : 1;
    }
}
