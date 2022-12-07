package org.example.pojo;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author Wang.Rui.Barney
 */
public class CovidBean implements Writable, WritableComparable<CovidBean> {
    private String province;
    private String city;
    private long diagnosedNum;
    private long deathNum;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getDiagnosedNum() {
        return diagnosedNum;
    }

    public void setDiagnosedNum(long diagnosedNum) {
        this.diagnosedNum = diagnosedNum;
    }

    public long getDeathNum() {
        return deathNum;
    }

    public void setDeathNum(long deathNum) {
        this.deathNum = deathNum;
    }

    @Override
    public String toString() {
        return "CovidBean{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", diagnosedNum=" + diagnosedNum +
                ", deathNum=" + deathNum +
                '}';
    }

    @Override
    public int compareTo(CovidBean o) {
        int i = province.compareTo(o.getProvince());

        if (i > 0) {
            return 1;
        } else if (i < 0) {
            return -1;
        } else {
            return Long.compare(o.getDiagnosedNum(), diagnosedNum);
        }
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(province);
        dataOutput.writeUTF(city);
        dataOutput.writeLong(diagnosedNum);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.province = dataInput.readUTF();
        this.city = dataInput.readUTF();
        this.diagnosedNum = dataInput.readLong();
    }
}
