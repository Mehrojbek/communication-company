package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.*;

@Data
public class ServiceDto {
    @NotNull
    private String name;

    @NotNull
    private Integer price;

    @NotNull
    private Integer duration;

    @NotNull
    private Integer serviceType;

    @NotNull
    private List<Integer> ussdCodes;
}
