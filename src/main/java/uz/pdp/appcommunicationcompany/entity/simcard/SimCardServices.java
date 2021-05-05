package uz.pdp.appcommunicationcompany.entity.simcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SimCardServices {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private SimCard simCard;

    @ManyToOne(optional = false)
    private Services service;

    @Column(nullable = false, updatable = false)
    private Date activatedDateService;


    @Transient
    private boolean isExpired;


    public boolean isExpired() {
        LocalDate date = activatedDateService.toLocalDate().plusDays(service.getDuration());
        boolean before = date.isBefore(LocalDate.now());
        return before;
    }
}
