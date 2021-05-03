package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.*;

@Data
public class PlanDto {

    @NotNull
    private String name;                                    //NOMI

    @NotNull
    private Integer switchPrice;                            //TARIFGA O'TISH NARXI

    @NotNull
    private Integer price;                                  //NARXI

    @NotNull
    private Integer duration;                               //QANCHA VAQTGA MO'LJALLANGAN

    @NotNull
    private List<DefaultPriceDto> defaultPriceDtoList;      //AGAR TERIF BO'YICHA BERILGAN PAKETLAR TUGASA DEFAULT NARXLAR

    @NotNull
    private List<Integer> clientType;                       //QAYSI TURDAGI MIJOZ UCHUN


    private List<PackageForPlanDto> packageForPlanDtos;      //TARIF BO'YICHA BERILGAN PAKETLAR //OPTIONAL


    private List<Integer> services;                          //TARIF BO'YICHA BERILGAN XIZMATLAR //OPTIONAL


}
