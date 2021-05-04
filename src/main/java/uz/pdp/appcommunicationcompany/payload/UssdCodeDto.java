package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UssdCodeDto {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String ussdCode;
}
