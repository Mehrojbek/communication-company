package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.Detalizatsiya;

import java.util.*;
import java.util.UUID;

public interface DetalizatsiyaRepository extends JpaRepository<Detalizatsiya, UUID> {
    List<Detalizatsiya> findAllBySimCardId(UUID simCard_id);
}
