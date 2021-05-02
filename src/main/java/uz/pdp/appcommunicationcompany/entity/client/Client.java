package uz.pdp.appcommunicationcompany.entity.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.appcommunicationcompany.entity.Role;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;

import javax.persistence.*;
import java.util.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Client{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false,length = 50)
    private String firstName;

    @Column(nullable = false,length = 50)
    private String lastName;

    @Column(unique = true,nullable = false)
    private String passport;               // BU CLIENT PASPORTI TIZIMDA UNIKAL USERNAME SIFATIDA KO'RILADI : AA1234567

    @ManyToOne
    private ClientType clientType;

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<SimCard> simCardList;






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
