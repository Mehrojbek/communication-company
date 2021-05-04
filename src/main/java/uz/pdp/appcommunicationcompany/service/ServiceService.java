package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.simcard.ServiceType;
import uz.pdp.appcommunicationcompany.entity.simcard.Services;
import uz.pdp.appcommunicationcompany.entity.ussd.UssdCode;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.ServiceDto;
import uz.pdp.appcommunicationcompany.repository.ServiceRepository;
import uz.pdp.appcommunicationcompany.repository.ServiceTypeRepository;
import uz.pdp.appcommunicationcompany.repository.UssdCodeRepository;

import java.util.*;

@Service
public class ServiceService {
    @Autowired
    UsefulMethods usefulMethods;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    ServiceTypeRepository serviceTypeRepository;
    @Autowired
    UssdCodeRepository ussdCodeRepository;


    public List<Services> getAll(){
        return serviceRepository.findAll();
    }


    public ApiResponse getOne(Integer id){
        Optional<Services> optionalService = serviceRepository.findById(id);
        return optionalService.map(services -> new ApiResponse("Ok", true, services)).orElseGet(() -> new ApiResponse("service topilmadi", false));
    }


    public ApiResponse add(ServiceDto serviceDto){
        if (usefulMethods.isPlanManagerOrDirector()) {
            boolean exists = serviceRepository.existsByName(serviceDto.getName());
            if (exists)
                return new ApiResponse("Bu servis avval yaratilgan",false);
            Services service = new Services();
                return setterFields(serviceDto,service,true);
            }
        return new ApiResponse("Xatolik",false);
    }


    public ApiResponse edit(Integer id, ServiceDto serviceDto){
        if (usefulMethods.isPlanManagerOrDirector()) {

            boolean exists = serviceRepository.existsByNameAndIdNot(serviceDto.getName(), id);
            if (exists)
                return new ApiResponse("Bu servis allaqachon mavjud",false);

            Optional<Services> optionalServices = serviceRepository.findById(id);
                if (!optionalServices.isPresent())
                    return new ApiResponse("Servis topilmadi",false);

                Services editingService = optionalServices.get();
                return setterFields(serviceDto,editingService,false);
        }
        return new ApiResponse("Xatolik",false);
    }


    public ApiResponse delete(Integer id){
        if (usefulMethods.isPlanManagerOrDirector()) {
            try {
                serviceRepository.deleteById(id);
                return new ApiResponse("Uchirildi",true);
            }catch (Exception e){
                return new ApiResponse("Uchirilmadi",false);
            }
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse setterFields(ServiceDto serviceDto, Services service, boolean isCreate){
        service.setName(serviceDto.getName());
        service.setPrice(serviceDto.getPrice());
        service.setDuration(serviceDto.getDuration());

        Optional<ServiceType> optionalServiceType = serviceTypeRepository.findById(serviceDto.getServiceType());
        if (!optionalServiceType.isPresent())
            return new ApiResponse("Service turi topilmadi",false);

        service.setServiceType(optionalServiceType.get());

        List<UssdCode> ussdCodes = ussdCodeRepository.findAllById(serviceDto.getUssdCodes());
        service.setUssdCodes(new HashSet<>(ussdCodes));

        serviceRepository.save(service);
        String message;
        if (isCreate){message = "Muvaffaqiyatli saqlandi";}else { message = "Muvaffaqiyatli tahrirlandi"; }

        return new ApiResponse(message,true);
    }
}
