package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.employee.Region;
import uz.pdp.appcommunicationcompany.entity.enums.RegionEnum;

public interface RegionRepository extends JpaRepository<Region,Integer> {
   Region findByName(RegionEnum name);
}
