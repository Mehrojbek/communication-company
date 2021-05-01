package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;
import uz.pdp.appcommunicationcompany.entity.simcard.ServiceType;

import java.util.UUID;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
}
