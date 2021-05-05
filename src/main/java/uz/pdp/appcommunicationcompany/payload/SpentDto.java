package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SpentDto {
    @NotNull
    private UUID simCardId;

    @NotNull
    private Double amount;

    @NotNull
    private String packageType;
}
