package uz.pdp.bot;

import lombok.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.model.Customer;
import uz.pdp.model.OrderItem;
import uz.pdp.model.Phone;
import uz.pdp.model.abs.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserActivity {
    private User user = new Customer();
    private int round;
    private int position;
    private List<OrderItem> phonesInPageList = new ArrayList<>();
    private List<OrderItem> myCart = new ArrayList<>();
    private Phone currentPhone;
    private Message msgFromBot;
    private int quantity = 1;
}
