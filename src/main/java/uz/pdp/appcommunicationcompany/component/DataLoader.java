package uz.pdp.appcommunicationcompany.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.appcommunicationcompany.entity.Role;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.employee.ManagerType;
import uz.pdp.appcommunicationcompany.entity.employee.Region;
import uz.pdp.appcommunicationcompany.entity.enums.*;
import uz.pdp.appcommunicationcompany.entity.payment.PaymentSystem;
import uz.pdp.appcommunicationcompany.entity.payment.PaymentUser;
import uz.pdp.appcommunicationcompany.entity.simcard.Code;
import uz.pdp.appcommunicationcompany.entity.simcard.CpeciesType;
import uz.pdp.appcommunicationcompany.entity.simcard.PackageType;
import uz.pdp.appcommunicationcompany.entity.simcard.ServiceType;
import uz.pdp.appcommunicationcompany.repository.*;

import java.util.*;
import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {
    @Value("${spring.datasource.initialization-mode}")
    String mode;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    CodeRepository codeRepository;
    @Autowired
    ServiceTypeRepository serviceTypeRepository;
    @Autowired
    ClientTypeRepository clientTypeRepository;
    @Autowired
    PaymentSystemRepository paymentSystemRepository;
    @Autowired
    PaymentUserRepository paymentUserRepository;
    @Autowired
    CpeciesTypeRepository cpeciesTypeRepository;
    @Autowired
    ManagerTypeRepository managerTypeRepository;
    @Autowired
    PackageTypeRepository packageTypeRepository;


    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("always")) {


            //SAVE ROLES
            List<Role> roles = new ArrayList<>();
            for (RoleNameEnum roleNameEnum : RoleNameEnum.values()) {
                Role role = new Role(roleNameEnum);
                roles.add(role);
            }
            roleRepository.saveAll(roles);


            //GET ROLE DIRECTOR
            Role roleDirector = roleRepository.findByName(RoleNameEnum.DIRECTOR);

            //SAVE DIRECTOR
            Employee employee = new Employee("Director", "Director", "director@gmail.com",
                    passwordEncoder.encode("director"), Collections.singleton(roleDirector), true);

            employeeRepository.save(employee);


            //SAVE REGIONS
            List<Region> regions = new ArrayList<>();
            for (RegionEnum regionName : RegionEnum.values()) {
                Region region = new Region();
                region.setName(regionName);
                regions.add(region);
            }
            regionRepository.saveAll(regions);


            //SAVE CODE
            for (CodeEnum codeEnum : CodeEnum.values()) {
                Code code = new Code();
                code.setCode(codeEnum.getCodeNumber());
                codeRepository.save(code);
            }


            //SAVE SERVICE TYPE
            for (ServiceTypeEnum serviceTypeEnum : ServiceTypeEnum.values()) {
                ServiceType serviceType = new ServiceType();
                serviceType.setServiceType(serviceTypeEnum);
                serviceTypeRepository.save(serviceType);
            }


            //SAVE CLIENT TYPE
            for (ClientTypeEnum clientTypeEnum : ClientTypeEnum.values()) {
                ClientType clientType = new ClientType();
                clientType.setType(clientTypeEnum);
                clientTypeRepository.save(clientType);
            }


            //SAVE PAYMENT SYSTEM
            for (PaymentSystemEnum paymentSystemEnum : PaymentSystemEnum.values()) {
                PaymentSystem paymentSystem = new PaymentSystem();
                paymentSystem.setPaymentSystem(paymentSystemEnum);
                paymentSystemRepository.save(paymentSystem);

                //SAVE PAYMENT USER
                Role rolePayment = roleRepository.findByName(RoleNameEnum.PAYMENT);

                PaymentUser paymentUser = new PaymentUser();
                paymentUser.setUsername(paymentSystemEnum.name());
                paymentUser.setPassword(passwordEncoder.encode(paymentSystemEnum.name()));
                paymentUser.setRoles(Collections.singleton(rolePayment));
                paymentUserRepository.save(paymentUser);
            }


            //SAVE CPECIES_TYPE SAVE
            for (CpeciesTypeEnum cpeciesTypeEnum : CpeciesTypeEnum.values()) {
                CpeciesType cpeciesType = new CpeciesType();
                cpeciesType.setCpeciesType(cpeciesTypeEnum);

                CpeciesType savedCpeciesType = cpeciesTypeRepository.save(cpeciesType);


                //SAVE PACKAGE_TYPE
                switch (cpeciesTypeEnum) {

                    case TARMOQ_ICHIDA:
                    case TARMOQDAN_TASHQARI:
                        PackageType sms = new PackageType();
                        PackageType minute = new PackageType();

                        sms.setCpeciesType(savedCpeciesType);
                        sms.setType(PackageTypeEnum.SMS);
                        packageTypeRepository.save(sms);

                        minute.setCpeciesType(savedCpeciesType);
                        minute.setType(PackageTypeEnum.MINUTE);
                        packageTypeRepository.save(minute);
                        break;

                    default:
                        PackageType mb = new PackageType();

                        mb.setCpeciesType(savedCpeciesType);
                        mb.setType(PackageTypeEnum.MB);
                        packageTypeRepository.save(mb);
                        break;
                }
            }


            //SAVE MANAGER_TYPE
            for (ManagerTypeEnum managerTypeEnum : ManagerTypeEnum.values()) {
                ManagerType managerType = new ManagerType();
                managerType.setManagerTypeEnum(managerTypeEnum);
                managerTypeRepository.save(managerType);
            }


        }
    }
}
