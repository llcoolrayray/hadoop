import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author w97766
 * @date 2021/12/6
 */
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
        configuration.set("fs.defaultFS", "hdfs://192.168.137.10:8020");
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
        Path path = new Path("/data");
        if (!fileSystem.exists(path)) {
            fileSystem.mkdirs(path);
        }
    }

    /**
     * 上传文件
     * @throws IOException
     */
    @Test
    public void upload() throws IOException {
        Path src = new Path("C:\\data.txt");
        Path dst = new Path("/data");
        fileSystem.copyFromLocalFile(src, dst);
    }

    /**
     * 下载文件
     * @throws IOException
     */
    @Test
    public void download() throws IOException {
        Path src = new Path("/data/data.txt");
        Path dst = new Path("C:\\code\\data.txt");
        fileSystem.copyToLocalFile(src, dst);
        logger.info("test");
    }
}
