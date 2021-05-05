package uz.pdp.appcommunicationcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.appcommunicationcompany.entity.enums.ActionType;
import uz.pdp.appcommunicationcompany.entity.payment.Payment;
import uz.pdp.appcommunicationcompany.entity.simcard.Package;
import uz.pdp.appcommunicationcompany.entity.simcard.Plan;
import uz.pdp.appcommunicationcompany.entity.simcard.Services;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Detalizatsiya {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private SimCard simCard;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private Double amount;            //OPTIONAL SIM KARTA AGAR MB, SMS, DAQIQA ISHLATSA AMOUNT
                                      // BULARDAN BIRINI QIYMATINI IFODALAYDI

    @ManyToOne
    private Package buyPackage;       //OPTIONAL PAKET SOTIB OLSA

    @ManyToOne
    private Payment doPayment;        //OPTIONAL TO'LOV QILSA

    @ManyToOne
    private Plan setPlan;              //OPTIONAL PLANNI O'ZGARTIRSA

    @ManyToOne
    private Services buyService;       //OPTIONAL SERVICE SOTIB OLSA

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
}
