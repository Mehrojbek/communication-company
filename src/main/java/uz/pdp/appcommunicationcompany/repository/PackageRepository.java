package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.simcard.Package;

public interface PackageRepository extends JpaRepository<Package,Integer> {
}
