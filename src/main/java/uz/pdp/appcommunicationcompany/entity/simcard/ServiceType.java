package uz.pdp.appcommunicationcompany.entity.simcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appcommunicationcompany.entity.enums.ServiceTypeEnum;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ServiceType {
    //XIZMAT TURI
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //AVVAL ENUMDA BOR BO'LGAN TURLAR SAQLANADI KEYINCHALIK YANGI SERVICE TURLARI CRUD QILINADI
    @Column(unique = true,nullable = false)
    private String serviceType;


}
