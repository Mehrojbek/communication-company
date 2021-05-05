package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCardServices;

import java.util.UUID;

public interface SimCardServiceRepository extends JpaRepository<SimCardServices, UUID> {
}
