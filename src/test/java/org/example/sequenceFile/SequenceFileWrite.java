package org.example.sequenceFile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;

import java.io.IOException;

public class SequenceFileWrite {
    private static final String[] DATA = {
            "a,b,c,d",
            "1,2,3,4",
            "bob,ted,barney,lee"
    };

    public static void main(String[] args) {
        //设置客户端身份，便于在 HDFS 上有权限操作文件
        System.setProperty("HADOOP_USER_NAME", "root");
        //用于指定相关参数
        Configuration configuration = new Configuration();
        //Sequence File key and value
        IntWritable key = new IntWritable();
        Text value = new Text();

        //构造 Writer 参数属性
        SequenceFile.Writer writer = null;
        CompressionCodec codec = new GzipCodec();

        SequenceFile.Writer.Option optPath = SequenceFile.Writer.file(new Path("hdfs://10.169.82.75:8020/seq.out"));
        SequenceFile.Writer.Option optKey = SequenceFile.Writer.keyClass(key.getClass());
        SequenceFile.Writer.Option optVal = SequenceFile.Writer.valueClass(value.getClass());
        SequenceFile.Writer.Option optCom = SequenceFile.Writer.compression(SequenceFile.CompressionType.RECORD, codec);

        try {
            writer = SequenceFile.createWriter(configuration, optPath, optKey, optVal, optCom);

            for (int i = 0; i < 100; i++) {
                key.set(100 - i);
                value.set(DATA[i%DATA.length]);
                System.out.printf("[%s]\t%s\t%s\n",writer.getLength(), key, value);
                writer.append(key, value);
            }

        }catch (Exception e) {{
            try {
                writer.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }}
    }
}
