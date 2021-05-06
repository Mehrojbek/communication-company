package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.Detalizatsiya;
import uz.pdp.appcommunicationcompany.entity.client.Client;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.employee.Branch;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.simcard.*;
import uz.pdp.appcommunicationcompany.entity.simcard.Package;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.ClientDto;
import uz.pdp.appcommunicationcompany.payload.GroupByPlan;
import uz.pdp.appcommunicationcompany.payload.SimcardDto;
import uz.pdp.appcommunicationcompany.repository.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

@Service
public class SimCardService {
    @Autowired
    UsefulMethods usefulMethods;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientTypeRepository clientTypeRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    CodeRepository codeRepository;
    @Autowired
    PlanRepository planRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    PackageRepository packageRepository;
    @Autowired
    SimCardServiceRepository simCardServiceRepository;
    @Autowired
    SimCardPackageRepository simCardPackageRepository;
    @Autowired
    DetalizatsiyaRepository detalizatsiyaRepository;



    public List<SimCard> getAll(){
        Employee numberManagerOrDirector = usefulMethods.getNumberManagerOrDirector();
        if (numberManagerOrDirector != null) {
            return simCardRepository.findAll();
        }
        return null;
    }


    public List<Detalizatsiya> getAllDetalizatsiya(){
        SimCard simCard = usefulMethods.getSimCard();
        if (simCard != null){
            List<Detalizatsiya> detalizatsiyas = detalizatsiyaRepository.findAllBySimCardId(simCard.getId());
            return detalizatsiyas;
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



    public List<SimCard> getAllSimCardsForBranchDirector(){
        Employee branchDirector = usefulMethods.getBranchDirector();
        if (branchDirector != null){
            List<SimCard> simCards = simCardRepository.findAllByBranchId(branchDirector.getBranch().getId());
            return simCards;
        }
        return null;
    }


    public List<SimCard> getAllPerQuarter(Integer quarterNumber){
        Employee director = usefulMethods.getDirector();
        if (director != null){
            if (quarterNumber == 1 || quarterNumber == 2 || quarterNumber == 3 || quarterNumber == 4) {
                int start = 0, end = 0;
                switch (quarterNumber) {
                    case 1:
                        start = 1;
                        end = 3;
                        break;
                    case 2:
                        start = 4;
                        end = 6;
                        break;
                    case 3:
                        start = 7;
                        end = 9;
                        break;
                    case 4:
                        start = 10;
                        end = 12;
                        break;
                }
                int maxLength = Month.of(end).maxLength();
                Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), start, 1), LocalTime.MIN));
                Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), end, maxLength), LocalTime.MAX));
                List<SimCard> simCards = simCardRepository.findAllByCreatedAtBetween(startTime, endTime);
                return simCards;
            }
        }
        return null;
    }


    public List<SimCard> getAllPerMonth(Integer monthNumber){
        Employee director = usefulMethods.getDirector();
        if (director != null){
            LocalDate start = LocalDate.of(LocalDate.now().getYear(), monthNumber, 1);
            int maxLength = start.getMonth().maxLength();
            if (start.isLeapYear() && monthNumber == 2)
                maxLength--;
            LocalDate end = LocalDate.of(LocalDate.now().getYear(), monthNumber, maxLength);

            Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(start, LocalTime.MIN));
            Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(end, LocalTime.MAX));
            List<SimCard> between = simCardRepository.findAllByCreatedAtBetween(startTime, endTime);
            return between;
        }
        return null;
    }


    public List<SimCard> getAllPerDay(Integer numberDayMonth){
        Employee director = usefulMethods.getDirector();
        if (director != null){
            if (numberDayMonth > 0 && numberDayMonth < 31) {
                Timestamp start = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), numberDayMonth), LocalTime.MIN));
                Timestamp end = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), numberDayMonth), LocalTime.MAX));
                List<SimCard> allByCreatedAtBetween = simCardRepository.findAllByCreatedAtBetween(start, end);
                return allByCreatedAtBetween;
            }
        }
        return null;
    }


    public List<GroupByPlan> getAllGroupByPlanOrderDesc(){
        Employee director = usefulMethods.getDirector();
        if (director != null){
            List<GroupByPlan> groupByPlans = simCardRepository.getAllSimCardGroupByPlanIdOrderDesc();
            for (GroupByPlan groupByPlan : groupByPlans) {
                Plan plan = planRepository.getOne(groupByPlan.getPlanId());
                groupByPlan.setPlanName(plan.getName());
            }
            return groupByPlans;
        }
        return null;
    }


    public ApiResponse add(SimcardDto simcardDto){
        Employee employee = usefulMethods.getEmployee();
        if (employee != null){
            boolean exists = simCardRepository.existsByNumber(simcardDto.getNumber());
            if (exists)
                return new ApiResponse("Bu simkarta avval band qilingan",false);
            SimCard simCard = new SimCard();
            return setterFields(employee,simCard,simcardDto,true);
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse edit(UUID id, SimcardDto simcardDto){
        Employee employee = usefulMethods.getEmployee();
        if (employee != null){
            boolean exists = simCardRepository.existsByNumberAndIdNot(simcardDto.getNumber(), id);
            if (exists)
                return new ApiResponse("Bu sim karta allaqachon mavjud",false);
            Optional<SimCard> optionalSimCard = simCardRepository.findById(id);
            if (!optionalSimCard.isPresent())
                return new ApiResponse("simkarta topilmadi",false);
            SimCard simCard = optionalSimCard.get();
            setterFields(employee,simCard,simcardDto,false);
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse delete(UUID id){
        Employee numberManagerOrDirector = usefulMethods.getNumberManagerOrDirector();
        if (numberManagerOrDirector != null){
            try {
                simCardRepository.deleteById(id);
                return new ApiResponse("uchirildi",true);
            }catch (Exception e){
                return new ApiResponse("uchirilmadi",false);
            }
        }
        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse setterFields(Employee employee, SimCard simCard, SimcardDto simcardDto, boolean isCreate){
        ClientDto clientDto = simcardDto.getClientDto();

        simCard.setNumber(simcardDto.getNumber());
        simCard.setBalance(simcardDto.getBalance());
        boolean exists = clientRepository.existsByPassport(clientDto.getPassport());
        if (exists){
            Client client = clientRepository.getByPassport(clientDto.getPassport());
            simCard.setClient(client);
        }else {
            Client client = new Client();
            client.setFirstName(clientDto.getFirstName());
            client.setLastName(clientDto.getLastName());
            client.setPassport(clientDto.getPassport());
            Optional<ClientType> optionalClientType = clientTypeRepository.findById(clientDto.getClientTypeId());
            if (!optionalClientType.isPresent())
                return new ApiResponse("mijoz turi topilmadi",false);
            client.setClientType(optionalClientType.get());
            client.setSimCardList(Collections.singletonList(simCard));

            Client savedClient = clientRepository.save(client);
            simCard.setClient(savedClient);
        }

        Optional<Branch> optionalBranch = branchRepository.findById(employee.getBranch().getId());
        if (!optionalBranch.isPresent())
            return new ApiResponse("Filial topilmadi",false);
        Branch branch = optionalBranch.get();
        simCard.setBranch(branch);

        Optional<Code> optionalCode = codeRepository.findById(simcardDto.getCodeId());
        if (!optionalCode.isPresent())
            return new ApiResponse("sim karta kodi topilmadi",false);
        Code code = optionalCode.get();
        simCard.setCode(code);

        Optional<Plan> optionalPlan = planRepository.findById(simcardDto.getPlanId());
        if (!optionalPlan.isPresent())
            return new ApiResponse("tarif reja topilmadi",false);
        Plan plan = optionalPlan.get();
        simCard.setPlan(plan);

        if (simcardDto.getPackages() != null) {
            List<Package> packages = packageRepository.findAllById(simcardDto.getPackages());
            for (Package aPackage : packages) {
                SimCardPackage simCardPackage = new SimCardPackage();

                simCardPackage.setSimCard(simCard);
                simCardPackage.setAPackage(aPackage);
                simCardPackage.setActivatedDatePackage(new Date(System.currentTimeMillis()));
            }
        }

        if (simcardDto.getServices() != null) {
            List<Services> services = serviceRepository.findAllById(simcardDto.getServices());
            for (Services service : services) {
                SimCardServices simCardServices = new SimCardServices();
                simCardServices.setSimCard(simCard);
                simCardServices.setService(service);
                simCardServices.setActivatedDateService(new Date(System.currentTimeMillis()));
            }
        }

        simCard.setActivatedDatePlan(new Date(System.currentTimeMillis()));
        simCard.setPassword(simCard.getPassword());
        simCardRepository.save(simCard);
        String message;
        if (isCreate){
            message = "Muvaffaqiaytli faollashtirildi";
        }else {
            message = "Muvaffaqiaytli tahrirlandi";
        }
        return new ApiResponse(message,true);
    }
}
