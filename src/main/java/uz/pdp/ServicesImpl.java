package uz.pdp;

import uz.pdp.services.servicesImpl.AdminServiceImpl;
import uz.pdp.services.servicesImpl.CustomerServiceImpl;

public class ServicesImpl {
    public static CustomerServiceImpl customerService = new CustomerServiceImpl();
    public static AdminServiceImpl adminService = new AdminServiceImpl();
}
