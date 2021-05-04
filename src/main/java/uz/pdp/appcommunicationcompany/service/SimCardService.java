package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.client.Client;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.simcard.Code;
import uz.pdp.appcommunicationcompany.entity.simcard.Package;
import uz.pdp.appcommunicationcompany.entity.simcard.Plan;
import uz.pdp.appcommunicationcompany.entity.simcard.Services;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.ClientDto;
import uz.pdp.appcommunicationcompany.payload.SimcardDto;
import uz.pdp.appcommunicationcompany.repository.*;

import java.util.*;

@Service
public class SimCardService {
    @Autowired
    UsefulMethods usefulMethods;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    CodeRepository codeRepository;
    @Autowired
    PlanRepository planRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    PackageRepository packageRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientTypeRepository clientTypeRepository;



    public List<SimCard> getAll(){
        if (usefulMethods.isEmployee()) {
            return simCardRepository.findAll();
        }
        return null;
    }



    public ApiResponse getOne(UUID id){
        if (usefulMethods.isEmployee()) {
            Optional<SimCard> optionalSimCard = simCardRepository.findById(id);
            return optionalSimCard.map(simCard -> new ApiResponse("Ok", true, simCard)).orElseGet(() -> new ApiResponse("Sim karta topilmadi", false));
        }
        if (usefulMethods.getSimCardUser() != null) {
            SimCard simCardUser = usefulMethods.getSimCardUser();
            if (id.equals(simCardUser.getId())){
                return new ApiResponse("Ok",true,simCardUser);
            }
        }
        return new ApiResponse("Xatolik",false);
    }


    public ApiResponse add(SimcardDto simcardDto){

        if (usefulMethods.isEmployee()) {
            boolean exists = simCardRepository.existsByNumber(simcardDto.getNumber());
            if (exists)
                return new ApiResponse("Bu raqam allaqachon mavjud",false);
            SimCard simCard = new SimCard();
            return setterFields(simCard,simcardDto,true);

        }
        return new ApiResponse("Xatolik",false);
    }


    public ApiResponse edit(UUID id, SimcardDto simcardDto){
        if (usefulMethods.isNumberManagerOrDirector()) {
            Optional<SimCard> optionalSimCard = simCardRepository.findById(id);
            if (!optionalSimCard.isPresent())
                return new ApiResponse("Sim karta topilmadi",false);

            boolean exists = simCardRepository.existsByNumberAndIdNot(simcardDto.getNumber(), id);
            if (exists)
                return new ApiResponse("Bu sim karta allaqachon mavjud",false);
            SimCard edititngSimCard = optionalSimCard.get();
            return setterFields(edititngSimCard,simcardDto,false);
        }
        return new ApiResponse("Xatolik",false);
    }


    public ApiResponse delete(UUID id){
        if (usefulMethods.isNumberManagerOrDirector()) {
            try {
                simCardRepository.deleteById(id);
                return new ApiResponse("Uchirildi",true);
            }catch (Exception e){}
            return new ApiResponse("Uchirilmadi",false);
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse setterFields(SimCard simCard, SimcardDto simcardDto, boolean isCreate){
        simCard.setNumber(simcardDto.getNumber());
        simCard.setBalance(simcardDto.getBalance());

        Optional<Code> optionalCode = codeRepository.findById(simcardDto.getCodeId());
        if (!optionalCode.isPresent())
            return new ApiResponse("Sim karta kodi topilmadi",false);
        Code code = optionalCode.get();

        simCard.setCode(code);

        Optional<Plan> optionalPlan = planRepository.findById(simcardDto.getPlanId());
        if (!optionalPlan.isPresent())
            return new ApiResponse("tarif reja topimadi",false);
        Plan plan = optionalPlan.get();

        simCard.setPlan(plan);

        if (simcardDto.getServices() != null) {
            List<Services> services = serviceRepository.findAllById(simcardDto.getServices());
            simCard.setServices(new HashSet<>(services));
        }

        if (simcardDto.getPackages() != null){
            List<Package> packages = packageRepository.findAllById(simcardDto.getPackages());
            simCard.setPackages(new HashSet<>(packages));
        }

        ClientDto clientDto = simcardDto.getClientDto();
        boolean exists = clientRepository.existsByPassport(clientDto.getPassport());
        if (exists){
            simCard.setClient(clientRepository.getByPassport(clientDto.getPassport()));

        }else {
            Client client = new Client();
            client.setFirstName(clientDto.getFirstName());
            client.setLastName(clientDto.getLastName());
            client.setPassport(clientDto.getPassport());

            Optional<ClientType> optionalClientType = clientTypeRepository.findById(clientDto.getClientTypeId());
            if (!optionalClientType.isPresent())
                return new ApiResponse("Mijoz turi topilmadi",false);
            ClientType clientType = optionalClientType.get();
            client.setClientType(clientType);
            Client savedClient = clientRepository.save(client);
            simCard.setClient(savedClient);
        }

         String message;
         if (isCreate){ message = "Muvaffaqiyatli saqlandi"; }else { message = "Muvaffaqiyatli tahrirlandi"; }

         return new ApiResponse(message, true);
    }
}
