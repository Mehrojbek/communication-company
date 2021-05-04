package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.ussd.UssdCode;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.UssdCodeDto;
import uz.pdp.appcommunicationcompany.repository.UssdCodeRepository;

import java.util.*;

@Service
public class UssdCodeService {
    @Autowired
    UsefulMethods usefulMethods;
    @Autowired
    UssdCodeRepository ussdCodeRepository;


    public List<UssdCode> getAll(){
        return ussdCodeRepository.findAll();
    }


    public ApiResponse getOne(Integer id){
        Optional<UssdCode> optionalUssdCode = ussdCodeRepository.findById(id);
        if (!optionalUssdCode.isPresent())
            return new ApiResponse("Ussd kod topilmadi",false);
        UssdCode ussdCode = optionalUssdCode.get();
        return new ApiResponse("Ok",true,ussdCode);
    }


    public ApiResponse add(UssdCodeDto ussdCodeDto){
        if (usefulMethods.isPlanManagerOrDirector()) {
            UssdCode ussdCode = new UssdCode();
            setterFields(ussdCode,ussdCodeDto,true);
        }
        return new ApiResponse("Xatolik",false);
    }

    public ApiResponse edit(Integer id, UssdCodeDto ussdCodeDto){
        if (usefulMethods.isPlanManagerOrDirector()) {
            Optional<UssdCode> optionalUssdCode = ussdCodeRepository.findById(id);
            if (!optionalUssdCode.isPresent())
                return new ApiResponse("Ussd kod topilmadi",false);
            UssdCode editingUssdCode = optionalUssdCode.get();
            setterFields(editingUssdCode,ussdCodeDto,false);
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse delete(Integer id){
        if (usefulMethods.isPlanManagerOrDirector()) {
            try {
                ussdCodeRepository.deleteById(id);
                return new ApiResponse("Uchirildi",true);
            }catch (Exception e){
                return new ApiResponse("Uchirilmadi",false);
            }
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse setterFields(UssdCode ussdCode, UssdCodeDto ussdCodeDto, boolean isCreate){
        ussdCode.setName(ussdCodeDto.getName());
        ussdCode.setDescription(ussdCodeDto.getDescription());
        ussdCode.setUssdCode(ussdCodeDto.getUssdCode());
        String message;
        if (isCreate){ message = "Mavaffaqiyatli saqlandi"; }else { message = "Muvaffaqiyatli tahrirlandi"; }
        return new ApiResponse(message,true);
    }
}
