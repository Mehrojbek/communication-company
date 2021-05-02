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
    private Integer switchPrice;//TARIFGA O'TISH NARXI

    @Column(nullable = false)
    private Integer price;//NARXI

    @Column(nullable = false)
    private Integer duration;//QANCHA VAQTGA MO'LJALLANGAN

    @OneToOne(fetch = FetchType.LAZY,optional = false,cascade = CascadeType.ALL)
    private DefaultPrice defaultPrice;//AGAR TERIF BO'YICHA BERILGAN PAKETLAR TUGASA DEFAULT NARXLAR

    @ManyToOne(optional = false)
    private ClientType clientType;//qaysi turdagi mijoz uchun

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Package> packages;//TARIF BO'YICHA BERILGAN PAKETLAR

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Service> services;//TARIF BO'YICHA BERILGAN XIZMATLAR





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
