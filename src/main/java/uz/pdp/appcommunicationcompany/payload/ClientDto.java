package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClientDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String passport;

    @NotNull
    private Integer clientTypeId;
}
