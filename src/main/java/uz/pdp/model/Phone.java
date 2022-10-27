package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Phone {
    private int id;
    private String imgUrl;
    private String brand;
    private String model;
    private int year;
    private long price;
    private int memory;

    public Phone(int id, String brand, String model, int year, long price, int memory) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.memory = memory;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", memory=" + memory +
                '}';
    }
}
