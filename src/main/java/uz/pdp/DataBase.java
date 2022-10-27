package uz.pdp;

import uz.pdp.bot.UserActivity;
import uz.pdp.model.*;
import uz.pdp.model.abs.User;

import java.time.LocalDate;
import java.util.*;

public abstract class DataBase {
    public static Map<Long, User> userMap = new HashMap<>();

    public static List<Phone> phoneList = new ArrayList<>();

    public static List<PayType> payTypeList = Arrays.asList();

    public static List<OrderHistory>  historyList = new ArrayList<>();

    public static Map<Long, UserActivity> userActivityMap = new HashMap<>();
}
