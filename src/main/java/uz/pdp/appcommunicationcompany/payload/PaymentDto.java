package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class PaymentDto {
    @NotNull
    private Double amount;

    @NotNull
    private UUID simCardId;

}
