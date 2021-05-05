package uz.pdp.appcommunicationcompany.entity.simcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DefaultPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private PackageType packageType;

    @Column(nullable = false)
    private Double price;
}
