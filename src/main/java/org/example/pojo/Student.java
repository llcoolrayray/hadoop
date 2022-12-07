package org.example.pojo;


import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Wang.Rui.Barney
 */
public class Student implements Writable, DBWritable {
    private long id;
    private String name;
    private String sex;
    private int age;

    public Student() {
        super();
    }

    public Student(long id, String name, String sex, int age) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }

    /**
     * 序列化方法
     *
     * @param out <code>DataOuput</code> to serialize this object into.
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(id);
        out.writeUTF(name);
        out.writeUTF(sex);
        out.writeInt(age);
    }

    /**
     * 反序列化方法
     *
     * @param in <code>DataInput</code> to deseriablize this object from.
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readLong();
        this.name = in.readUTF();
        this.sex = in.readUTF();
        this.age = in.readInt();
    }

    /**
     * 数据库写数据
     *
     * @param statement the statement that the fields are put into.
     * @throws SQLException
     */
    @Override
    public void write(PreparedStatement statement) throws SQLException {
        statement.setLong(1, id);
        statement.setString(2, name);
        statement.setString(3, sex);
        statement.setInt(4, age);
    }

    /**
     * 数据库读数据
     *
     * @param resultSet the {@link ResultSet} to get the fields from.
     * @throws SQLException
     */
    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong(1);
        this.name = resultSet.getString(2);
        this.sex = resultSet.getString(3);
        this.age = resultSet.getInt(4);
    }
}
