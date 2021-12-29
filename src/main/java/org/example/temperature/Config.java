package org.example.temperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Map;

/**
 * @author w97766
 * @date 2021/11/29
 */
public class Config extends Configured implements Tool {

    static {
        Configuration.addDefaultResource("test");
    }


    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = getConf();
        for (Map.Entry<String, String> entry : configuration) {
            System.out.println(entry.getKey()+"---"+entry.getValue());
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Config(), args);
        System.exit(exitCode);
    }
}
