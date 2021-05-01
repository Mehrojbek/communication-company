package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.employee.ManagerType;
import uz.pdp.appcommunicationcompany.entity.simcard.Code;

public interface ManagerTypeRepository extends JpaRepository<ManagerType, Integer> {
}
