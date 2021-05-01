package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.payment.PaymentSystem;
import uz.pdp.appcommunicationcompany.entity.simcard.ServiceType;

public interface PaymentSystemRepository extends JpaRepository<PaymentSystem, Integer> {
}
