package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.employee.Turniket;

import java.util.UUID;

public interface TurniketRepository extends JpaRepository<Turniket, UUID> {
}
