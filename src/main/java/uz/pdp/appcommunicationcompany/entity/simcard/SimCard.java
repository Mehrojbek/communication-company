package uz.pdp.appcommunicationcompany.entity.simcard;

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
import uz.pdp.appcommunicationcompany.entity.client.Client;
import uz.pdp.appcommunicationcompany.entity.employee.Branch;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)

public class SimCard implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true,nullable = false)
    private String number;              //UNIKAL RAQAM USERNAME SIFATIDA KO'RILADI

    @Column(nullable = false)
    private Double balance;             //BALANS

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
    private Client client;              //SIM KARTA EGASI

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Branch branch;              //FILIAL ID

    @ManyToOne(optional = false)
    private Code code;                  //SIM KARTA KODI

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Plan plan;                  //TA'RIF REJA

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Services> services;      //QO'SHIMCHA OLINGAN XIZMATLAR

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Package> packages;      //QO'SHIMCHA OLINGAN PAKETLAR



    @Column(nullable = false)
    private String password;            //BU SIM KARTA HAM TIZIMGA KIRA OLISHI UCHUN

    @ManyToMany
    private Set<Role> roles;            //ROL SIM_CARD


    private boolean accountNonExpired=true;

    private boolean accountNonLocked=true;

    private boolean credentialsNonExpired=true;

    private boolean enabled=true;





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








    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return number;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
