package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.employee.Region;
import uz.pdp.appcommunicationcompany.entity.enums.RegionEnum;

public interface ClientTypeRepository extends JpaRepository<ClientType,Integer> {

}
