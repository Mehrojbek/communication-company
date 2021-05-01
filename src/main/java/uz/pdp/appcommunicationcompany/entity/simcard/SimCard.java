package uz.pdp.appcommunicationcompany.entity.simcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.appcommunicationcompany.entity.client.Client;
import uz.pdp.appcommunicationcompany.entity.employee.Branch;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SimCard {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true,nullable = false)
    private String number;              //UNIKAL RAQAM

    @Column(nullable = false)
    private Double balance;             //BALANS

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Client client;              //SIM KARTA EGASI

    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;              //FILIAL ID

    @ManyToOne(optional = false)
    private Code code;                  //SIM KARTA KODI

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Plan plan;                  //TA'RIF REJA

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<ServiceForBuy> services;//QO'SHIMCHA OLINGAN XIZMATLAR

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<PackageForBuy> packages;//QO'SHIMCHA OLINGAN PAKETLAR









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
