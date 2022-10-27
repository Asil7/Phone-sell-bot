package uz.pdp.model;

import uz.pdp.model.enums.Role;
import uz.pdp.model.abs.User;

public class Admin extends User {
    public Admin() {
    }

    public Admin(long id, String firstName, String lastName, String userName) {
        super(id, firstName, lastName, userName);
    }

    public Admin(long id, String firstName, String lastName, String phoneNumber, String userName, Role role) {
        super(id, firstName, lastName, phoneNumber, userName, role);
    }
}
