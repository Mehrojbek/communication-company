package uz.pdp.appcommunicationcompany.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.appcommunicationcompany.entity.Role;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.employee.Branch;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.employee.ManagerType;
import uz.pdp.appcommunicationcompany.entity.employee.Region;
import uz.pdp.appcommunicationcompany.entity.enums.*;
import uz.pdp.appcommunicationcompany.entity.simcard.Code;
import uz.pdp.appcommunicationcompany.entity.simcard.CpeciesType;
import uz.pdp.appcommunicationcompany.entity.simcard.PackageType;
import uz.pdp.appcommunicationcompany.entity.simcard.ServiceType;
import uz.pdp.appcommunicationcompany.entity.ussd.UssdCode;
import uz.pdp.appcommunicationcompany.repository.*;

import java.nio.charset.StandardCharsets;
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
    CpeciesTypeRepository cpeciesTypeRepository;
    @Autowired
    ManagerTypeRepository managerTypeRepository;
    @Autowired
    PackageTypeRepository packageTypeRepository;
    @Autowired
    UssdCodeRepository ussdCodeRepository;
    @Autowired
    BranchRepository branchRepository;


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


            //SAVE REGIONS AND BRANCH
            List<Branch> branches = new ArrayList<>();
            for (RegionEnum regionName : RegionEnum.values()) {
                Region region = new Region();
                region.setName(regionName);

                Branch branch = new Branch();
                branch.setRegion(region);
                branch.setName(regionName.name());
                branches.add(branch);
            }

            branchRepository.saveAll(branches);


            //SAVE CODE
            for (CodeEnum codeEnum : CodeEnum.values()) {
                Code code = new Code();
                code.setCode(codeEnum.getCodeNumber());
                codeRepository.save(code);
            }


            //SAVE SERVICE TYPE //KEYINCHALIK BOSHQA SERVICE TURLARI HAM QO'SHILISHI MUMKIN MANAGER TOMONIDAN
            for (ServiceTypeEnum serviceTypeEnum : ServiceTypeEnum.values()) {
                ServiceType serviceType = new ServiceType();
                serviceType.setServiceType(serviceTypeEnum.name());
                serviceTypeRepository.save(serviceType);
            }


            //SAVE CLIENT TYPE
            for (ClientTypeEnum clientTypeEnum : ClientTypeEnum.values()) {
                ClientType clientType = new ClientType();
                clientType.setType(clientTypeEnum);
                clientTypeRepository.save(clientType);
            }




            //SAVE PAYMENT SYSTEM

                Role rolePayment = roleRepository.findByName(RoleNameEnum.PAYMENT_USER);

                Employee oson = new Employee();
                oson.setFirstName("OSON");
                oson.setLastName("oson");
                oson.setEmail("oson");
                oson.setPassword(passwordEncoder.encode("oson"));
                oson.setRoles(Collections.singleton(rolePayment));
                oson.setEnabled(true);

                employeeRepository.save(oson);


                Employee paynet = new Employee();
                paynet.setFirstName("Paynet");
                paynet.setLastName("paynet");
                paynet.setEmail("paynet");
                paynet.setPassword(passwordEncoder.encode("paynet"));
                paynet.setRoles(Collections.singleton(rolePayment));
                paynet.setEnabled(true);

                employeeRepository.save(paynet);


                Employee payme = new Employee();
                payme.setFirstName("Payme");
                payme.setLastName("payme");
                payme.setEmail("payme");
                payme.setPassword(passwordEncoder.encode("payme"));
                payme.setRoles(Collections.singleton(rolePayment));
                payme.setEnabled(true);

                employeeRepository.save(payme);





            //SAVE CPECIES_TYPE SAVE //KEYINCHALIK BOSHQA TURLAR HAM QO'SHILISHI MUMKIN MANAGER TOMONIDAN
            for (CpeciesTypeEnum cpeciesTypeEnum : CpeciesTypeEnum.values()) {
                CpeciesType cpeciesType = new CpeciesType();
                cpeciesType.setCpeciesType(cpeciesTypeEnum.name());

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


            //SAVE ANY USSD CODE
            for (BaseUssdEnum baseUssdEnum : BaseUssdEnum.values()) {
                UssdCode ussdCode = new UssdCode();

                ussdCode.setName(baseUssdEnum.name().toLowerCase());
                ussdCode.setDescription(baseUssdEnum.name().toLowerCase());
                ussdCode.setUssdCode(baseUssdEnum.getUssdCode());

                ussdCodeRepository.save(ussdCode);
            }


            //SAVE MANAGER_TYPE //KEYINCHALIK BOSHQA MANAGER TURI HAM QO'SHILISHI MUMKIN
            for (ManagerTypeEnum managerTypeEnum : ManagerTypeEnum.values()) {
                ManagerType managerType = new ManagerType();
                managerType.setManagerType(managerTypeEnum.name());
                managerTypeRepository.save(managerType);
            }


        }
    }
}
