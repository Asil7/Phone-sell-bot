package uz.pdp.model.abs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.model.Phone;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class AbsPhonesQuantity {
    private Phone phone;
    private int quantity;
}
