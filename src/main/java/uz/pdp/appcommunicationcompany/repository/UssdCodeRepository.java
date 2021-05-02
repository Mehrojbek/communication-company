package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.simcard.Code;
import uz.pdp.appcommunicationcompany.entity.ussd.UssdCode;

public interface UssdCodeRepository extends JpaRepository<UssdCode, Integer> {
}
