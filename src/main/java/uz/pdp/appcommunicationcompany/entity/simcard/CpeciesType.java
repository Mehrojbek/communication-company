package uz.pdp.appcommunicationcompany.entity.simcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appcommunicationcompany.entity.enums.CpeciesTypeEnum;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CpeciesType {
    //BU TARMOQ ICHIDA YOKI TARMOQDAN TASHQARI MINUTE, SMS VA HOKAZOLAR TURI
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //BU STRING BO'LISHIGA SABAB QO'SHIMCHA ENUM DA MAVJUD BO'LMAGAN TURLAR QO'SHILISHI MUMKIN
    @Column(unique = true)
    private String cpeciesType;

}
