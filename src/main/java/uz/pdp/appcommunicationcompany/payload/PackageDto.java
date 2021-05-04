package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.*;

@Data
public class PackageDto {
    @NotNull
    private Integer amount;
    @NotNull
    private Integer duration;
    @NotNull
    private Integer price;
    @NotNull
    private Integer packageType;
    @NotNull
    private List<Integer> ussdCode;
    @NotNull
    private Integer clientType;
}
