package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DefaultPriceDto {
    @NotNull
    private Integer packageTypeId;

    @NotNull
    private Double price;
}
