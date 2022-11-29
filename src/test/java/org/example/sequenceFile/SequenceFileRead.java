package org.example.sequenceFile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;

public class SequenceFileRead {
    public static void main(String[] args) {
        //设置客户端身份，便于在 HDFS 上有权限操作文件
        System.setProperty("HADOOP_USER_NAME", "root");
        //用于指定相关参数
        Configuration configuration = new Configuration();

        SequenceFile.Reader.Option option1 = SequenceFile.Reader.file(new Path("hdfs://10.169.82.75:8020/seq.out"));
        // 指定要读取的字节数
        SequenceFile.Reader.Option option2 = SequenceFile.Reader.length(1024);

        SequenceFile.Reader reader = null;
        try {
            reader = new SequenceFile.Reader(configuration, option1, option2);
            Writable key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), configuration);
            Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), configuration);
            long position = reader.getPosition();

            while (reader.next(key, value)) {
                String syncSeen = reader.syncSeen()?"*":"";
                System.out.printf("[%s%s]\t%s\t%s\n",position, syncSeen, key, value);
            }


        }catch (Exception e) {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
