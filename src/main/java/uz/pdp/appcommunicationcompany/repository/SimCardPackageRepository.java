package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.employee.Branch;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCardPackage;

import java.util.UUID;

public interface SimCardPackageRepository extends JpaRepository<SimCardPackage, UUID> {
}
