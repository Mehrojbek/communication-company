package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class VerifyDto {
    @NotNull
    @Size(min = 3,max = 50)
    private String firstName;

    @NotNull
    @Size(min = 3,max = 50)
    private String lastName;

    @NotNull
    @Size(min = 8,max = 100)
    private String password;
}
