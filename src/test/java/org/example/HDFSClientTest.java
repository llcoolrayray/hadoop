package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author w97766
 * @date 2021/12/6
 */
@Ignore
public class HDFSClientTest {
    private static Configuration configuration;
    private static FileSystem fileSystem;
    Logger logger = Logger.getLogger(HDFSClientTest.class);

    @Before
    public void connectHDFS() throws IOException {
        //设置客户端身份，便于在 HDFS 上有权限操作文件
        System.setProperty("HADOOP_USER_NAME", "root");

        configuration = new Configuration();
        //设置操作的文件系统是 HDFS，并指定 HDFS 的操作地址
        configuration.set("fs.defaultFS", "hdfs://10.169.82.75:8020");
        fileSystem = FileSystem.get(configuration);
    }

    @After
    public void close() {
        try {
            if (null != fileSystem) {
                fileSystem.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录
     * @throws IOException
     */
    @Test
    public void mkdir() throws IOException {
        Path path = new Path("/barney");
        if (!fileSystem.exists(path)) {
            fileSystem.mkdirs(path);
        }
    }

    /**
     * 删除目录
     * @throws IOException
     */
    @Test
    public void deleteMkdir() throws IOException {
        Path path = new Path("/barney");
        if (fileSystem.exists(path)) {
            fileSystem.delete(path, true);
        }
    }


    /**
     * 上传文件
     * @throws IOException
     */
    @Test
    public void upload() throws IOException {
        Path src = new Path("D:\\files\\配置邮件服务器.txt");
        Path dst = new Path("/test/");
        fileSystem.copyFromLocalFile(src, dst);
    }

    /**
     * 下载文件
     * @throws IOException
     */
    @Test
    public void download() throws IOException {
        Path src = new Path("/barney/lol.txt");
        Path dst = new Path("C:\\lol.txt");
        fileSystem.copyToLocalFile(src, dst);
        logger.info("test");
    }

    @Test
    public void test() throws IOException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(new Date()));
    }
}
