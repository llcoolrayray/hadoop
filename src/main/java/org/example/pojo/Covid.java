package org.example.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Wang.Rui.Barney
 */
public class Covid {
    private String date;
    private String city;
    private String province;
    private long diagnosedNum;
    private long deathNum;

    public Covid(String date, String city, String province, long diagnosedNum, long deathNum) {
        this.date = date;
        this.city = city;
        this.province = province;
        this.diagnosedNum = diagnosedNum;
        this.deathNum = deathNum;
    }

    private static String getRandomDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long start = 0;
        try {
            start = sdf.parse("2019-12-1").getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long end = 0;
        try {
            end = sdf.parse("2022-12-31").getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Random random = new Random();
        long randomDate = start + (long) (random.nextDouble() * (end - start + 1));

        return sdf.format(new Date(randomDate));
    }

    private static long getRandomDiagnosedNum() {
        Random random = new Random();
        return random.ints(0, 1000).findFirst().getAsInt();
    }

    private static long getRandomDeathNum() {
        Random random = new Random();
        return random.ints(0, 500).findFirst().getAsInt();
    }

    private static JsonNode getCityFile() {
        try {
            StringBuilder stringBuffer = new StringBuilder();
            BufferedReader in = new BufferedReader(new FileReader("D:\\barney\\code\\docs\\hadoop\\src\\test\\resources\\city.json"));
            String str;
            while ((str = in.readLine()) != null) {
                stringBuffer.append(str);
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(stringBuffer.toString());

        } catch (IOException ignored) {
        }
        return null;
    }

    private static Pair<String, String> getRandomCity() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode cityObject = getCityFile();

        Random random = new Random();
        List<String> provinces = new ArrayList<>();
        cityObject.fieldNames().forEachRemaining(provinces::add);

        String randomProvince = provinces.get(random.nextInt(provinces.size() - 1));
        List<String> cities = mapper.convertValue(cityObject.findValue(randomProvince), List.class);
        String city = cities.get(random.nextInt(cities.size() - 1));

        return new Pair<>(randomProvince, city);
    }

    public static Covid newInstace() {
        Pair<String, String> pair = getRandomCity();
        String date = getRandomDate();
        String province = pair.getKey();
        String city = pair.getValue();
        long diagnosedNum = getRandomDiagnosedNum();
        long deathNum = getRandomDeathNum();

        return new Covid(date, city, province, diagnosedNum, deathNum);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Covid.newInstace());
        System.out.println(Covid.newInstace());
        System.out.println(Covid.newInstace());
        System.out.println(Covid.newInstace());
        System.out.println(Covid.newInstace());

        File file = new File("D:\\barney\\code\\docs\\hadoop\\src\\test\\resources\\data.text");
        for (int i = 0; i < 1000000; i++) {
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            output.write(Covid.newInstace().toData());
            output.write("\r\n");
            output.flush();
            output.close();
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    private String getRandomProvince() {
        return null;
    }

    public String toData() {
        return date + "," + province + "," + city + "," + diagnosedNum + "," + deathNum;
    }
}
