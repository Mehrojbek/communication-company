package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.Detalizatsiya;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.employee.ManagerType;
import uz.pdp.appcommunicationcompany.entity.enums.ActionType;
import uz.pdp.appcommunicationcompany.entity.enums.ManagerTypeEnum;
import uz.pdp.appcommunicationcompany.entity.simcard.Package;
import uz.pdp.appcommunicationcompany.entity.simcard.PackageType;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCardPackage;
import uz.pdp.appcommunicationcompany.entity.ussd.UssdCode;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.PackageDto;
import uz.pdp.appcommunicationcompany.repository.*;

import java.sql.Date;
import java.util.*;

@Service
public class PackageService {
    @Autowired
    UsefulMethods usefulMethods;
    @Autowired
    PackageRepository packageRepository;
    @Autowired
    PackageTypeRepository packageTypeRepository;
    @Autowired
    UssdCodeRepository ussdCodeRepository;
    @Autowired
    ClientTypeRepository clientTypeRepository;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    SimCardPackageRepository simCardPackageRepository;
    @Autowired
    DetalizatsiyaRepository detalizatsiyaRepository;




    public List<Package> getAll(){
        return packageRepository.findAll();
    }



    public ApiResponse getOne(Integer id){
        Optional<Package> optionalPackage = packageRepository.findById(id);
        return optionalPackage.map(aPackage -> new ApiResponse("Ok", true, aPackage)).orElseGet(() -> new ApiResponse("Paket topilmadi", false));
    }



    public ApiResponse add(PackageDto packageDto){
        if (usefulMethods.isPlanManagerOrDirector()) {

                Package aPackage = new Package();
                return setterFields(aPackage, packageDto,true);
            }

        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse edit(Integer id, PackageDto packageDto){

        if (usefulMethods.isPlanManagerOrDirector()) {

                Optional<Package> optionalPackage = packageRepository.findById(id);
                if (!optionalPackage.isPresent())
                    return new ApiResponse("Paket topilmadi", false);
                Package editingPackage = optionalPackage.get();
                return setterFields(editingPackage,packageDto,false);

        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse delete(Integer id){

        if (usefulMethods.isPlanManagerOrDirector()) {
            try {
                packageRepository.deleteById(id);
                return new ApiResponse("uchirildi", true);
            } catch (Exception e) {
                return new ApiResponse("Uchirilmadi", false);
            }
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse buy(Integer packageId){
        SimCard simCard = usefulMethods.getSimCard();
        if (simCard != null){
            Optional<Package> optionalPackage = packageRepository.findById(packageId);
            if (!optionalPackage.isPresent())
                return new ApiResponse("Paket topilmadi",false);
            Package aPackage = optionalPackage.get();

            Double balance = simCard.getBalance();

            if (balance > aPackage.getPrice()){

                Detalizatsiya detalizatsiya = new Detalizatsiya();
                detalizatsiya.setActionType(ActionType.BUY_PACKAGE);
                detalizatsiya.setSimCard(simCard);
                detalizatsiya.setBuyPackage(aPackage);
                detalizatsiyaRepository.save(detalizatsiya);

                simCard.setBalance(simCard.getBalance() - aPackage.getPrice());

                //SIM KARTADA BOR BO'LGAN PAKETLAR
                Set<SimCardPackage> packages = simCard.getPackages();

                //SIM_CARD DA AVVALDAN OLINGAN PAKETLAR MAVJUD BO'LSA
                if (packages != null) {
                    for (SimCardPackage simCardPackage : packages) {
                        Package currentPackage = simCardPackage.getAPackage();
                        if (currentPackage.getPackageType().equals(aPackage.getPackageType())){
                            boolean expired = simCardPackage.isExpired();
                            if (expired)
                                break;
                            currentPackage.setAmount(currentPackage.getAmount() + aPackage.getAmount());
                            currentPackage.setDuration(currentPackage.getDuration() + aPackage.getDuration());
                        }
                    }
                }else {
                    //AGAR PAKETLAR BO'LMASA
                    SimCardPackage simCardPackage = new SimCardPackage();

                    simCardPackage.setAPackage(aPackage);
                    simCardPackage.setActivatedDatePackage(new Date(System.currentTimeMillis()));
                    simCardPackage.setSimCard(simCard);

                }
                simCardRepository.save(simCard);
                return new ApiResponse("Muvaffaqiyatli faollashtirildi",true);
            }

        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse setterFields(Package aPackage, PackageDto packageDto, boolean isCreate){
        aPackage.setAmount(packageDto.getAmount());
        aPackage.setDuration(packageDto.getDuration());
        aPackage.setPrice(packageDto.getPrice());

        Optional<PackageType> optionalPackageType = packageTypeRepository.findById(packageDto.getPackageType());
        if (!optionalPackageType.isPresent())
            return new ApiResponse("paket turi topilmadi",false);

        aPackage.setPackageType(optionalPackageType.get());

        List<UssdCode> ussdCodes = ussdCodeRepository.findAllById(packageDto.getUssdCode());

        aPackage.setUssdCode(new HashSet<>(ussdCodes));

        Optional<ClientType> optionalClientType = clientTypeRepository.findById(packageDto.getClientType());
        if (!optionalClientType.isPresent())
            return new ApiResponse("Mijoz turi topilmadi",false);

        aPackage.setClientType(optionalClientType.get());
        packageRepository.save(aPackage);

        String message;

        if (isCreate){ message = "Muvafaqiyatli saqlandi"; }else { message = "Muvaffaqiyatli tahrirlandi"; }

        return new ApiResponse(message,true);
    }
}
