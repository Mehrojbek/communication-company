package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;
import uz.pdp.appcommunicationcompany.entity.simcard.Package;

import javax.validation.constraints.NotNull;
import java.util.*;

@Data
public class SimcardDto {

    @NotNull
    private String number;

    @NotNull
    private Double balance;

    @NotNull
    private ClientDto clientDto;

    @NotNull
    private Integer branchId;

    @NotNull
    private Integer codeId;

    @NotNull
    private Integer planId;


    private List<Integer> services;


    private List<Integer> packages;
}
