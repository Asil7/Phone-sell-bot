package uz.pdp.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.bot.UserActivity;

public interface CustomerService {
    void showCustomerMenu(UserActivity currentUser, Message message, SendMessage sendMessage);
    String showPhones(UserActivity currentUserActivity);
    void myCart();
    void orderHistory();
}
