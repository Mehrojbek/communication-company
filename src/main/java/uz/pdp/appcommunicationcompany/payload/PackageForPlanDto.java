package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class PackageForPlanDto {
    @NotNull
    private Double amount;

    @NotNull
    private Integer packageTypeId;

}
