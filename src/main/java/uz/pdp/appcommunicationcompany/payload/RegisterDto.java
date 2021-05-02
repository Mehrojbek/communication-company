package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.*;

@Data
public class RegisterDto {

    @NotNull
    @Size(min = 3,max = 50)
    private String firstName;

    @NotNull
    @Size(min = 3,max = 50)
    private String lastName;

    @NotNull
    @Email
    private String email;           //username

    @NotNull
    @Size(min = 8,max = 100)
    private String password;

    private Integer managerType;    //agar bu manager bo'lsa qaysi turdagi manager

    private Integer branch;         //agar bu xodim bo'lsa qaysi filial xodimi

    @NotNull
    private Integer role;    //

}
