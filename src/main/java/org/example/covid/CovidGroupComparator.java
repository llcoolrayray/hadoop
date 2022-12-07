package org.example.covid;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.example.pojo.CovidBean;

/**
 * @author Wang.Rui.Barney
 */
public class CovidGroupComparator extends WritableComparator {

    public CovidGroupComparator() {
        super(CovidBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        JSONObject jsonObject = new JSONObject();
        System.out.println(jsonObject);
        CovidBean aBean = (CovidBean) a;
        CovidBean bBean = (CovidBean) b;

        return aBean.getProvince().compareTo(bBean.getProvince());
    }
}
