package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;

import java.util.Optional;
import java.util.UUID;

public interface SimCardRepository extends JpaRepository<SimCard, UUID> {
    boolean existsByNumber(String number);
    boolean existsByNumberAndIdNot(String number, UUID id);
    Optional<SimCard> findByNumber(String number);
}
