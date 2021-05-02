package uz.pdp.appcommunicationcompany.entity.simcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.ussd.UssdCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Package {
    //BU QO'SHIMCHA SOTIB OLISH UCHUN PAKET
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer amount;//MIQDORI

    private Integer duration;//MUDDATI

    private Integer price;//NARXI

    @ManyToOne(optional = false)
    private PackageType packageType;//PAKET TURI : MB, SMS, MINUTE

    @OneToMany(fetch = FetchType.LAZY)
    private Set<UssdCode> ussdCode;     //SHU PAKETNI UCHUN USSD KOD LAR UCHIRISH, YOQISH,... VA HOKAZO


    @ManyToMany(fetch = FetchType.LAZY)
    private Set<ClientType> clientTypes; //BU PAKET KIM UCHUN JISMONIY, YURIDIK, JISMONIY-YURIDIK









    //AUDITING UCHUN
    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(updatable = false)
    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;
}
