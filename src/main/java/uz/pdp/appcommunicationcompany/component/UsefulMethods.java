package uz.pdp.appcommunicationcompany.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.employee.ManagerType;
import uz.pdp.appcommunicationcompany.entity.enums.ManagerTypeEnum;
import uz.pdp.appcommunicationcompany.entity.enums.RoleNameEnum;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;
import uz.pdp.appcommunicationcompany.repository.ManagerTypeRepository;
import uz.pdp.appcommunicationcompany.repository.SimCardRepository;

import java.util.UUID;

@Component
public class UsefulMethods {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    ManagerTypeRepository managerTypeRepository;
    @Autowired
    SimCardRepository simCardRepository;

    //USERNI ROLINI ANIQLASH
    public byte getRoleNumber() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {

            //1----
            try {
                SimCard simCard = (SimCard) principal;
                for (GrantedAuthority authority : simCard.getAuthorities()) {
                    if (authority.getAuthority().equals(RoleNameEnum.SIM_CARD.name()))
                        return 0;
                }
            } catch (Exception e) {}

            //2----
            try {
                Employee employee = (Employee) principal;
                for (GrantedAuthority authority : employee.getAuthorities()) {
                    if (authority.getAuthority().equals(RoleNameEnum.WORKER.name()))
                        return 1;
                    if (authority.getAuthority().equals(RoleNameEnum.BRANCH_DIRECTOR.name()))
                        return 2;
                    if (authority.getAuthority().equals(RoleNameEnum.MANAGER.name()))
                        return 3;
                    if (authority.getAuthority().equals(RoleNameEnum.DIRECTOR.name()))
                        return 4;
                    if (authority.getAuthority().equals(RoleNameEnum.PAYMENT_USER.name()))
                        return 10;
                }
            } catch (Exception e) {
            }
        }
        return -1;
    }



    public boolean isPlanManagerOrDirector(){
        try {
            byte roleNumber = getRoleNumber();
            Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ManagerType managerType = employee.getManagerType();

            if ((managerType != null && managerType.getManagerType().equals(ManagerTypeEnum.PLAN_MANAGER.name())) || roleNumber == 4){
                return true;
            }

        }catch (Exception e){ }
        return false;
    }


    public boolean isNumberManagerOrDirector(){
        try {
            byte roleNumber = getRoleNumber();
            if (roleNumber == 3 || roleNumber == 4) {
                Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                ManagerType managerType = employee.getManagerType();
                if ((managerType != null && managerType.getManagerType().equals(ManagerTypeEnum.NUMBER_MANAGER.name())) || roleNumber == 4){
                    return true;
                }
            }
        }catch (Exception e){}
        return false;
    }


    public boolean isEmployee(){
        try {
            byte roleNumber = getRoleNumber();
            if (roleNumber == 1 || roleNumber == 2 || roleNumber == 3 || roleNumber == 4) {
                return true;
            }
        }catch (Exception e){}
        return false;
    }


    public Employee getEmployee(){
        byte roleNumber = getRoleNumber();
        if (roleNumber == 1 || roleNumber == 2 || roleNumber == 3 || roleNumber == 4){
            Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return employee;
        }
        return null;
    }


    public Employee getBranchDirectorOrDirector(){
        byte roleNumber = getRoleNumber();
        if (roleNumber == 2 || roleNumber == 4){
            Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return employee;
        }
        return null;
    }


    public SimCard getSimCard(){
        byte roleNumber = getRoleNumber();
        if (roleNumber == 0){
            SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return simCard;
        }
        return null;
    }


    public Employee getPaymentUser(){
        byte roleNumber = getRoleNumber();
        if (roleNumber == 10){
            Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return employee;
        }
        return null;
    }


    public Employee getNumberManagerOrDirector(){
        byte roleNumber = getRoleNumber();
        if (roleNumber == 3 || roleNumber == 4){
            Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ManagerType managerType = employee.getManagerType();
            if ((managerType != null && managerType.getManagerType().equals(ManagerTypeEnum.NUMBER_MANAGER.name())) || roleNumber == 4){
               return employee;
            }
        }
        return null;
    }


    //MAIL SENDER
    public boolean sendEmail(String sendingEmail, String message, boolean verifyEmail, String subject) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            String text;

            if (verifyEmail) {
                text = "http://localhost:8080/api/auth/verifyEmail?email=" + sendingEmail + "&emailCode=" + message;
            } else {
                text = message;
            }

            mailMessage.setFrom("Beeline");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
