package uz.pdp;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.pdp.model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static uz.pdp.DataBase.*;

public abstract class DataJson {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void getAllDataFromJson() {
        getUsersFromJson();

        phoneList = readDataFromJson(
                new com.google.gson.reflect.TypeToken<ArrayList<Phone>>() {
                }.getType(), "src/main/resources/jsons/phoneList.json");

//        payTypeList = readDataFromJson(
//                new com.google.gson.reflect.TypeToken<ArrayList<PayType>>() {
//                }.getType(), "src/main/resources/jsons/payTypeList.json");

        historyList = readDataFromJson(
                new com.google.gson.reflect.TypeToken<ArrayList<OrderHistory>>() {
                }.getType(), "src/main/resources/jsons/historyList.json");

    }

    public static void writer(Object obj, String filePath) {
        try (Writer writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(gson.toJson(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T readDataFromJson(Type type, String inputFilePath) {
        try {
            return gson.fromJson(new FileReader(inputFilePath), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getUsersFromJson() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/jsons/admin.json"))) {
            Admin admin = gson.fromJson(reader, Admin.class);
            DataBase.userMap.put(admin.getId(), admin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Reader reader = new FileReader("src/main/resources/jsons/customers.json")) {
            List<Customer> customers = gson.fromJson(reader, new TypeToken<ArrayList<Customer>>() {
            }.getType());
            for (Customer customer : customers) {
                DataBase.userMap.put(customer.getId(), customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
