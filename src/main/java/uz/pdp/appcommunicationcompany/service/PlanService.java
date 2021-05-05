package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.Detalizatsiya;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;

import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.enums.ActionType;
import uz.pdp.appcommunicationcompany.entity.simcard.*;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.DefaultPriceDto;
import uz.pdp.appcommunicationcompany.payload.PackageForPlanDto;
import uz.pdp.appcommunicationcompany.payload.PlanDto;
import uz.pdp.appcommunicationcompany.repository.*;

import java.sql.Date;
import java.util.*;

@Service
public class PlanService {
    @Autowired
    PlanRepository planRepository;
    @Autowired
    ClientTypeRepository clientTypeRepository;
    @Autowired
    UsefulMethods usefulMethods;
    @Autowired
    ManagerTypeRepository managerTypeRepository;
    @Autowired
    PackageTypeRepository packageTypeRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    DefaultPriceRepository defaultPriceRepository;
    @Autowired
    SimCardRepository  simCardRepository;
    @Autowired
    DetalizatsiyaRepository detalizatsiyaRepository;



    //GET ALL PLAN
    public List<Plan> getAll(){
        return planRepository.findAll();
    }



    //GET ONE PLAN
    public ApiResponse getOne(Integer id){
        Optional<Plan> optionalPlan = planRepository.findById(id);
        return optionalPlan.map(plan -> new ApiResponse("Muvaffaqiyatli", true, plan)).orElseGet(() -> new ApiResponse("Toplimadi", false));
    }



    //ADD PLAN
    public ApiResponse add(PlanDto planDto){
        if (usefulMethods.isPlanManagerOrDirector()) {

            boolean exists = planRepository.existsByName(planDto.getName());
            if (exists)
                return new ApiResponse("Bu ta'rif reja allaqachon mavjud",false);

            Plan plan = new Plan();

                plan.setName(planDto.getName());
                plan.setPrice(planDto.getPrice());

                plan.setSwitchPrice(planDto.getSwitchPrice());
                plan.setDuration(planDto.getDuration());

                //ADD PACKAGES
                Set<PackageForPlan> packageForPlans = new HashSet<>();
                if (planDto.getPackageForPlanDtos()!=null) {
                    for (PackageForPlanDto packageForPlanDto : planDto.getPackageForPlanDtos()) {
                        PackageForPlan packageForPlan = new PackageForPlan();
                        Optional<PackageType> optionalPackageType = packageTypeRepository.findById(packageForPlanDto.getPackageTypeId());
                        if (!optionalPackageType.isPresent())
                            return new ApiResponse("Xatolik", false);
                        packageForPlan.setPackageType(optionalPackageType.get());
                        packageForPlan.setAmount(packageForPlanDto.getAmount());

                        //PACKAGES LIST
                        packageForPlans.add(packageForPlan);
                    }
                    //ADD PACKAGES TO PLAN
                    plan.setPackageForPlans(packageForPlans);
                }




                //DEFAULT_PRICE LARNI YARATIB CHIQISH       //DEFAULT_PRICE_LIST PLAN_DTO DA KELGAN
                for (DefaultPriceDto defaultPriceDto : planDto.getDefaultPriceDtoList()) {
                    DefaultPrice defaultPrice = new DefaultPrice();
                    //HAR BIR DEFAULT PRICE GA JORIY PLANNI SET QILIB QO'YAMIZ
                    defaultPrice.setPlan(plan);
                    Optional<PackageType> optionalPackageType = packageTypeRepository.findById(defaultPriceDto.getPackageTypeId());
                    if (!optionalPackageType.isPresent())
                        return new ApiResponse("Xatolik",false);
                    defaultPrice.setPackageType(optionalPackageType.get());
                    defaultPrice.setPrice(defaultPriceDto.getPrice());

                }



                //PLANGA SERVICE LARNI QO'SHAMIZ //AGAR BO'LSA
                if (planDto.getServices() != null) {
                    //SET SERVICES TO PLAN
                    plan.setServices(new HashSet<>(serviceRepository.findAllById(planDto.getServices())));
                }

                //BU PLAN JISMONIY SHAXS, YOKI YURIDIK SHAXS, YOKI IKKALSI UCHUN
                Optional<ClientType> optionalClientType = clientTypeRepository.findById(planDto.getClientType());
                if (!optionalClientType.isPresent())
                    return new ApiResponse("mijoz turi aniqlanmadi",false);

                plan.setClientType(optionalClientType.get());
                planRepository.save(plan);
                return new ApiResponse("Muvaffaqiyatli yaratildi",true);
            }
        return new ApiResponse("Xatolik",false);
    }



    //EDIT PLAN
    public ApiResponse edit(Integer id, PlanDto planDto){

        if (usefulMethods.isPlanManagerOrDirector()) {

            boolean exists = planRepository.existsByNameAndIdNot(planDto.getName(), id);
            if (exists)
                return new ApiResponse("Bu nomdagi tarif reja allaqachon mavjud",false);

            Optional<Plan> optionalPlan = planRepository.findById(id);
                if (!optionalPlan.isPresent())
                    return new ApiResponse("Tarif reja topilmadi", false);

                Plan editingPlan = optionalPlan.get();


                editingPlan.setName(planDto.getName());
                editingPlan.setSwitchPrice(planDto.getSwitchPrice());
                editingPlan.setPrice(planDto.getPrice());
                editingPlan.setDuration(planDto.getDuration());


                //EDITING_PLAN DAGI DEFAULT_PRICE LAR
                Set<DefaultPrice> defaultPrices = editingPlan.getDefaultPrices();
                defaultPriceRepository.deleteAll(defaultPrices);


                //EDITING_PLAN DAGI DEFAULT_PRICE LARGA PLAN_DTO GAI QIYMATLARNI SET QILIB CHIQAMIZ
                for (DefaultPriceDto defaultPriceDto : planDto.getDefaultPriceDtoList()) {
                    DefaultPrice defaultPrice = new DefaultPrice();
                    defaultPrice.setPrice(defaultPriceDto.getPrice());
                    defaultPrice.setPlan(editingPlan);
                    Optional<PackageType> optionalPackageType = packageTypeRepository.findById(defaultPriceDto.getPackageTypeId());
                    if (!optionalPackageType.isPresent())
                        return new ApiResponse("Paket turi topilmadi",false);
                    defaultPrice.setPackageType(optionalPackageType.get());
                }


                //SET CLIENT TYPE
                Optional<ClientType> optionalClientType = clientTypeRepository.findById(planDto.getClientType());
                if (!optionalClientType.isPresent())
                    return new ApiResponse("Xatolik",false);
                editingPlan.setClientType(optionalClientType.get());


                //GET ALL PACKAGES AND ADD TO SET
                if (planDto.getPackageForPlanDtos() != null) {
                    Set<PackageForPlan> packageForPlans = new HashSet<>();
                    for (PackageForPlanDto packageForPlanDto : planDto.getPackageForPlanDtos()) {
                        PackageForPlan packageForPlan = new PackageForPlan();
                        packageForPlan.setAmount(packageForPlanDto.getAmount());
                        Optional<PackageType> optionalPackageType = packageTypeRepository.findById(packageForPlanDto.getPackageTypeId());
                        if (!optionalPackageType.isPresent())
                            return new ApiResponse("Paket toifasi topilmadi",false);
                        packageForPlan.setPackageType(optionalPackageType.get());
                        packageForPlans.add(packageForPlan);
                    }
                    //ADD PACKAGES_SET SET EDITING_PLAN
                    editingPlan.setPackageForPlans(packageForPlans);
                }


                if (planDto.getServices() != null){
                    //SET SERVICES TO EDITING PLAN
                    editingPlan.setServices((new HashSet<>(serviceRepository.findAllById(planDto.getServices()))));
                }

                planRepository.save(editingPlan);
                return new ApiResponse("Muvaffaqiyatli tahrirlandi",true);
        }
        return null;
    }



    public ApiResponse setPlan(Integer planId){
        SimCard simCard = usefulMethods.getSimCard();

        if (simCard != null ){
            Optional<Plan> optionalPlan = planRepository.findById(planId);
            if (!optionalPlan.isPresent())
                return new ApiResponse("tarif reja topilmadi",false);
            Plan plan = optionalPlan.get();

            Double balance = simCard.getBalance();

            if (balance > (plan.getPrice() + plan.getSwitchPrice()) ){
                ClientType simCardClientType = simCard.getClient().getClientType();
                ClientType planClientType = plan.getClientType();

                if ( simCardClientType.equals(planClientType) || planClientType.getId() == 3 ){

                    Detalizatsiya detalizatsiya = new Detalizatsiya();
                    detalizatsiya.setSimCard(simCard);
                    detalizatsiya.setActionType(ActionType.SET_PLAN);
                    detalizatsiya.setSetPlan(plan);
                    detalizatsiyaRepository.save(detalizatsiya);

                    simCard.setBalance(simCard.getBalance() - (plan.getSwitchPrice() + plan.getPrice()));
                    simCard.setPlan(plan);
                    simCard.setActivatedDatePlan(new Date(System.currentTimeMillis()));
                    simCardRepository.save(simCard);
                    return new ApiResponse("Tarifga muvaffaqiyatli o'tdingiz",true);
                }
            }
        }
        return new ApiResponse("Xatolik",false);
    }
}
