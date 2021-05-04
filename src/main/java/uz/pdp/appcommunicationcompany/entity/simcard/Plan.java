package uz.pdp.appcommunicationcompany.entity.simcard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Plan {
    //TARIF REJASI
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;//NOMI

    @Column(nullable = false)
    private Integer switchPrice;            //TARIFGA O'TISH NARXI

    @Column(nullable = false)
    private Integer price;                  //NARXI

    @Column(nullable = false)
    private Integer duration;               //QANCHA VAQTGA MO'LJALLANGAN

    @JsonIgnore
    @OneToMany(mappedBy = "plan",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<DefaultPrice> defaultPrices;        //AGAR TARIF BO'YICHA BERILGAN PAKETLAR TUGASA DEFAULT NARXLAR

    @ManyToOne(cascade = CascadeType.ALL)
    private ClientType clientType;                  //QAYSI TURDAGI MIJOZ UCHUN : JISMONIY, YURIDIK, IKKALASI

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<PackageForPlan> packageForPlans;    //TARIF BO'YICHA BERILGAN PAKETLAR //OPTIONAL

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Services> services;                  //TARIF BO'YICHA BERILGAN XIZMATLAR //OPTIONAL





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
