package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.simcard.DefaultPrice;

public interface DefaultPriceRepository extends JpaRepository<DefaultPrice,Integer> {
}
