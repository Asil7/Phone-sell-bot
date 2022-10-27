package uz.pdp.model;

import uz.pdp.model.abs.AbsPhonesQuantity;

public class StoreItem extends AbsPhonesQuantity {
    public StoreItem() {
    }

    public StoreItem(Phone phone, int quantity) {
        super(phone, quantity);
    }
}
