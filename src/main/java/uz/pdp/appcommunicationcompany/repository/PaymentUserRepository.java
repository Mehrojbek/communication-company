package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.payment.PaymentUser;

import java.util.UUID;

public interface PaymentUserRepository extends JpaRepository<PaymentUser, UUID> {

}
