package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.SimcardDto;
import uz.pdp.appcommunicationcompany.repository.SimCardRepository;

import java.util.*;

@Service
public class SimCardService {
    @Autowired
    UsefulMethods usefulMethods;
    @Autowired
    SimCardRepository simCardRepository;

    public List<SimCard> getAll(){
        Employee numberManagerOrDirector = usefulMethods.getNumberManagerOrDirector();
        if (numberManagerOrDirector != null) {
            return simCardRepository.findAll();
        }
        return null;
    }



    public ApiResponse getOne(UUID id) {
        Employee employee = usefulMethods.getEmployee();
        if (employee != null){
            Optional<SimCard> optionalSimCard = simCardRepository.findById(id);
            if (!optionalSimCard.isPresent())
                return new ApiResponse("sim karta topilmadi",false);
            return new ApiResponse("Ok",true,optionalSimCard.get());
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse add(SimcardDto simcardDto){

    }
}
