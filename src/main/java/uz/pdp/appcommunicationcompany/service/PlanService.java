package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.employee.ManagerType;
import uz.pdp.appcommunicationcompany.entity.enums.ClientTypeEnum;
import uz.pdp.appcommunicationcompany.entity.enums.ManagerTypeEnum;
import uz.pdp.appcommunicationcompany.entity.simcard.*;
import uz.pdp.appcommunicationcompany.entity.simcard.Package;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.DefaultPriceDto;
import uz.pdp.appcommunicationcompany.payload.PackageForPlanDto;
import uz.pdp.appcommunicationcompany.payload.PlanDto;
import uz.pdp.appcommunicationcompany.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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



    //GET ALL PLAN
    public ApiResponse getAll(boolean physicalPerson){

        List<Plan> allPlansByClientType =
                physicalPerson?planRepository.getAllPlansByClientType(ClientTypeEnum.JISMONIY.name())
                :planRepository.getAllPlansByClientType(ClientTypeEnum.YURIDIK.name());

        return new ApiResponse("Muvaffaqiyatli",true,allPlansByClientType);
    }



    //GET ONE PLAN
    public ApiResponse getOne(Integer id){
        Optional<Plan> optionalPlan = planRepository.findById(id);
        return optionalPlan.map(plan -> new ApiResponse("Muvaffaqiyatli", true, plan)).orElseGet(() -> new ApiResponse("Toplimadi", false));
    }



    //ADD PLAN
    public ApiResponse add(PlanDto planDto){
        byte roleNumber = usefulMethods.getRoleNumber();
        if (roleNumber == 3 || roleNumber == 4){
            Employee manager = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ManagerType managerType = manager.getManagerType();
            //BU USER PLAN MANGER YOKI DIRECTOR
            if (managerType.getManagerType().equals(ManagerTypeEnum.PLAN_MANAGER.name()) || roleNumber == 4){
                Plan plan = new Plan();

                plan.setName(planDto.getName());
                plan.setPrice(planDto.getPrice());
                plan.setSwitchPrice(planDto.getSwitchPrice());
                plan.setDuration(planDto.getDuration());

                //PLANDAGI BO'SH PACKAGE
                Set<PackageForPlan> packageForPlans = plan.getPackageForPlans();
                for (PackageForPlanDto packageForPlanDto : planDto.getPackageForPlanDtos()) {
                    PackageForPlan packageForPlan = new PackageForPlan();
                    Optional<PackageType> optionalPackageType = packageTypeRepository.findById(packageForPlanDto.getPackageTypeId());
                    if (!optionalPackageType.isPresent())
                        return new ApiResponse("Xatolik",false);
                    packageForPlan.setPackageType(optionalPackageType.get());
                    packageForPlan.setAmount(packageForPlan.getAmount());

                    //PLANDAN OLINGA BO'SH PACKAGE_FOR_PLANS GA PACKAGE_FOR_PLAN LARNI QO'SHISH
                    packageForPlans.add(packageForPlan);
                }

                //PLANDAGI BO'SH DEFAULT PRICE
                Set<DefaultPrice> defaultPrices = plan.getDefaultPrices();

                //DEFAULT PRICE LARNI YARATIB CHIQISH
                for (DefaultPriceDto defaultPriceDto : planDto.getDefaultPriceDtoList()) {
                    DefaultPrice defaultPrice = new DefaultPrice();
                    defaultPrice.setPlan(plan);
                    Optional<PackageType> optionalPackageType = packageTypeRepository.findById(defaultPriceDto.getPackageTypeId());
                    if (!optionalPackageType.isPresent())
                        return new ApiResponse("Xatolik",false);
                    defaultPrice.setPackageType(optionalPackageType.get());
                    defaultPrice.setPrice(defaultPriceDto.getPrice());

                    //PLANDAGI BO'SH DEFAULT PRICE GA DEFAULT PRICE LARNI QO'SHISH
                    defaultPrices.add(defaultPrice);
                }


                //PLAN DAGI BO'SH SERVICE LARNI OLIB UNGA SERVICE_REPOSITORYDAN KERAKLI SERVICE LARNI QO'SHISH
                Set<uz.pdp.appcommunicationcompany.entity.simcard.Service> services = plan.getServices();
                services.addAll(serviceRepository.findAllById(planDto.getServices()));
                List<ClientType> clientTypes = clientTypeRepository.findAllById(planDto.getClientType());

                plan.setClientType(new HashSet<>(clientTypes));
                planRepository.save(plan);
                return new ApiResponse("Muvaffaqiyatli yaratildi",true);
            }
        }
        return new ApiResponse("Xatolik",false);
    }
}
