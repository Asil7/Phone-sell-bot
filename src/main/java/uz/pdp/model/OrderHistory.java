package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderHistory {
    private int id;
    private Customer client;
    private double totalPrice;
    private PayType payType;
    private List<OrderItem>orderItems = new ArrayList<>();
    private LocalDate date = LocalDate.now();


}
