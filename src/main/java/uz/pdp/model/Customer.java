package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.model.enums.Role;
import uz.pdp.model.abs.User;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer extends User {
    List<OrderItem> orderItemList = new ArrayList<>();

    public Customer(long id, String firstName, String lastName, String userName) {
        super(id, firstName, lastName, userName);
    }

    public Customer(long id, String firstName, String lastName, String phoneNumber, String userName, Role role) {
        super(id, firstName, lastName, phoneNumber, userName, role);
    }

}
