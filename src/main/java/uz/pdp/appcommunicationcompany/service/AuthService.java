package uz.pdp.appcommunicationcompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.appcommunicationcompany.component.UsefulMethods;
import uz.pdp.appcommunicationcompany.entity.Role;
import uz.pdp.appcommunicationcompany.entity.employee.Branch;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.employee.ManagerType;
import uz.pdp.appcommunicationcompany.entity.employee.Turniket;
import uz.pdp.appcommunicationcompany.entity.enums.RoleNameEnum;
import uz.pdp.appcommunicationcompany.entity.payment.PaymentUser;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.LoginDto;
import uz.pdp.appcommunicationcompany.payload.RegisterDto;
import uz.pdp.appcommunicationcompany.payload.VerifyDto;
import uz.pdp.appcommunicationcompany.repository.*;
import uz.pdp.appcommunicationcompany.security.JwtProvider;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UsefulMethods usefulMethods;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    ManagerTypeRepository managerTypeRepository;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    PaymentUserRepository paymentUserRepository;
    @Autowired
    JwtProvider jwtProvider;

    //REGISTER
    public ApiResponse register(RegisterDto registerDto){

        //TIZIMDAGI USERNI ROLI RAQAMI
        byte roleNumber = usefulMethods.getRoleNumber();

        //QO'SHILAYOYGAN EMPLOYEE NI ROLINI ANIQLASH
        Optional<Role> optionalEmployeeRole = roleRepository.findById(registerDto.getRole());
        if (!optionalEmployeeRole.isPresent())
            return new ApiResponse("Rol topilmadi",false);
        Role employeeRole = optionalEmployeeRole.get();

        //ADD WORKER // BRANCH_DIRECTOR OR MANAGER OR DIRECTOR
        if ((employeeRole.getName().equals(RoleNameEnum.WORKER)) && (roleNumber == 2 || roleNumber == 3 || roleNumber == 4))
            return setAllFieldForRegister(employeeRole.getName(),registerDto);

        //ADD BRANCH_DIRECTOR // MANAGER OR DIRECTOR
        if ((employeeRole.getName().equals(RoleNameEnum.BRANCH_DIRECTOR)) && (roleNumber == 3 || roleNumber == 4))
            return setAllFieldForRegister(employeeRole.getName(),registerDto);

        //ADD MANAGER  // ONLY DIRECTOR
        if ((employeeRole.getName().equals(RoleNameEnum.MANAGER)) && roleNumber == 4)
            return setAllFieldForRegister(employeeRole.getName(),registerDto);

        return new ApiResponse("Xatolik",false);
    }


    //VERIFY
    public ApiResponse verify(VerifyDto verifyDto, String email, String emailCode){
        Optional<Employee> optionalEmployee = employeeRepository.findByEmailAndEmailCode(email, emailCode);
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Bu xodim allaqachon faollashtirilgan",false);
        Employee employee = optionalEmployee.get();
        employee.setEmailCode(null);
        employee.setEnabled(true);

        employeeRepository.save(employee);
        return new ApiResponse("Muvaffaqiyatli tasdiqlandi",true);
    }


    //LOGIN
    public ApiResponse login(LoginDto loginDto){
        try {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));

            String token = jwtProvider.generateToken(loginDto.getUsername(), authenticate.getAuthorities());
            return new ApiResponse("Muvaffaqiyatli bajarildi",true,token);
        }catch (Exception e){ e.printStackTrace(); }
        return new ApiResponse("login yoki parol xato",false);
    }




    //YORDAMCHI METHOD
    public ApiResponse setAllFieldForRegister(RoleNameEnum roleNameEnum, RegisterDto registerDto ){
        //UNIKAL XODIM EMAILINI TEKSHIRISH
        boolean exists = employeeRepository.existsByEmail(registerDto.getEmail());
        if (exists)
            return new ApiResponse("Bu username allaqachon mavjud",false);


        Employee employee = new Employee();

        employee.setFirstName(registerDto.getFirstName());
        employee.setLastName(registerDto.getLastName());
        employee.setEmail(registerDto.getEmail());
        employee.setPassword(registerDto.getPassword());
        employee.setEmailCode(UUID.randomUUID().toString());

        //SET BRANCH
        if (roleNameEnum.equals(RoleNameEnum.WORKER) || roleNameEnum.equals(RoleNameEnum.BRANCH_DIRECTOR)){
            if (registerDto.getBranch() == null)
                return new ApiResponse("Filial bo'sh bo'lmasligi kerak",false);
            Optional<Branch> optionalBranch = branchRepository.findById(registerDto.getBranch());
            if (!optionalBranch.isPresent())
                return new ApiResponse("Filial topilmadi",false);
            Branch branch = optionalBranch.get();
            employee.setBranch(branch);
        }

        //SET MANAGER_TYPE
        if (roleNameEnum.equals(RoleNameEnum.MANAGER)){
            if (registerDto.getManagerType() == null)
                return new ApiResponse("manager turi bo'sh bo;lmasligi kerak",false);
            Optional<ManagerType> optionalManagerType = managerTypeRepository.findById(registerDto.getManagerType());
            if (!optionalManagerType.isPresent())
                return new ApiResponse("Manager turi topilmadi",false);
            ManagerType managerType = optionalManagerType.get();
            employee.setManagerType(managerType);
        }

        long cardNumber = turniketRepository.count() + 1000_0000_0000_0000l;

        //TURNIKET BERISH
        Turniket turniket = new Turniket();
        turniket.setCardNumber(String.valueOf(cardNumber));
        turniket.setEmployee(employee);

        employee.setTurniket(turniket);

        employeeRepository.save(employee);

        //EMAILGA XABAR YUBORISH
        usefulMethods.sendEmail(employee.getEmail(), employee.getEmailCode(), true, "Emailni tasdiqlash");
        return new ApiResponse("Muvaffaqiyatli saqlandi",true);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /**TIZIMGA 3 XIL USER KIRISHI MUMKIN : PAYMENT_USER, EMPLOYEE, SIM_CARD
        SIMKARTADA UNIKAL FIELDI BU RAQAMI, PAYMENT_USERDA O'ZIMIZ KIRITGAN USERNAME, EMPLOYEE DA EMAIL
        ULAR BIR-BIRIGA TENG BO'LISHI MUMKIN EMAS SABABI 1-SI RAQAM, 2-SI SO'Z, 3-SI EMAIL
        TIZIMGA KIRMOQCHI BO'LGAN USER EMPLOYEE BO'LSA**/

        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(username);
        if (optionalEmployee.isPresent())
            return optionalEmployee.get();
        //TIZIMGA KIRMOQCHI BO'LGAN USER SIM_CARD BO'LSA
        Optional<SimCard> optionalSimCard = simCardRepository.findByNumber(username);
        if (optionalSimCard.isPresent())
            return optionalSimCard.get();
        //TIZIMGA KIRMOQCHI BO'LGAN USER TO'LOV TIZIMI BO'LSA
        Optional<PaymentUser> optionalPaymentUser = paymentUserRepository.findByUsername(username);
        if (optionalPaymentUser.isPresent())
            return optionalPaymentUser.get();
        //XATOLIK
        throw new UsernameNotFoundException(username);
    }
}
