package uz.pdp.appcommunicationcompany.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.enums.RoleNameEnum;
import uz.pdp.appcommunicationcompany.entity.payment.PaymentUser;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;

@Component
public class UsefulMethods {
    @Autowired
    JavaMailSender javaMailSender;

    //USERNI ROLINI ANIQLASH
    public byte getRoleNumber(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){

            SimCard simCard = (SimCard) principal;
            for (GrantedAuthority authority : simCard.getAuthorities()) {
                if (authority.getAuthority().equals(RoleNameEnum.SIM_CARD.name()))
                    return 0;
            }

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
            }

            PaymentUser paymentUser = (PaymentUser) principal;
            for (GrantedAuthority authority : paymentUser.getAuthorities()) {
                if (authority.getAuthority().equals(RoleNameEnum.PAYMENT_USER.name()))
                    return 10;
            }
        }
        return -1;
    }




    //MAIL SENDER
    public boolean sendEmail(String sendingEmail, String message, boolean verifyEmail, String subject) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            String text;

            if (verifyEmail) {
                text = "http://localhost:8080/api/auth/verifyEmail?email=" + sendingEmail + "&emailCode=" + message;
            } else { text = message; }

            mailMessage.setFrom("Company");
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
