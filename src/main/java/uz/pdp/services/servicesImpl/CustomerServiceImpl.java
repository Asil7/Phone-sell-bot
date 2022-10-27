package uz.pdp.services.servicesImpl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.bot.UserActivity;
import uz.pdp.model.OrderItem;
import uz.pdp.model.Phone;
import uz.pdp.services.CustomerService;

import java.util.ArrayList;

import static uz.pdp.DataBase.phoneList;
import static uz.pdp.bot.OnlinePhoneSellBot.*;

public class CustomerServiceImpl implements CustomerService {
    @Override
    public void showCustomerMenu(UserActivity currentUserActivity, Message message, SendMessage sendMessage) {
        sendMessage.setText("Main menu");
        currentUserActivity.setRound(2);
        sendMessage.setReplyMarkup(getReplyKeyboard(currentUserActivity));
    }

    @Override
    public String showPhones(UserActivity currentUserActivity) {
        currentUserActivity.setPhonesInPageList(new ArrayList<>());
        StringBuilder res = new StringBuilder();
        int pos = 1;
        int start = currentUserActivity.getPosition();
        int end = Math.min(start + 10, phoneList.size());
        for (int i = start; i < end; i++) {
            Phone phone = phoneList.get(i);
            res.append(pos).append(" ").append(phone.getBrand()).append(" ").append(phone.getModel()).append(" ").append(phone.getYear()).append("\n");
            pos++;
            currentUserActivity.getPhonesInPageList().add(new OrderItem(phone,currentUserActivity.getQuantity()));
        }
        return res.toString();
    }

    @Override
    public void myCart() {

    }

    @Override
    public void orderHistory() {

    }
}
