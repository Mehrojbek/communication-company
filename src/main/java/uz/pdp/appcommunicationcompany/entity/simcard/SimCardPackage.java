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
public class SimCardPackage {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private SimCard simCard;

    @ManyToOne
    private Package aPackage;

    @Column(nullable = false, updatable = false)
    private Date activatedDatePackage;

    @Transient
    private boolean isExpired;


    public boolean isExpired() {
        LocalDate date = activatedDatePackage.toLocalDate().plusDays(aPackage.getDuration());
        return date.isBefore(LocalDate.now());
    }
}
