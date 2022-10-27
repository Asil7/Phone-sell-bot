package uz.pdp.model;

import uz.pdp.model.abs.AbsPhonesQuantity;

public class OrderItem extends AbsPhonesQuantity {
    public OrderItem(Phone phone, int quantity) {
        super(phone, quantity);
    }
}
