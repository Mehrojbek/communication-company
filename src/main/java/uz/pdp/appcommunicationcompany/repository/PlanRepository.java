package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.appcommunicationcompany.entity.client.ClientType;
import uz.pdp.appcommunicationcompany.entity.simcard.Plan;

import java.util.*;

public interface PlanRepository extends JpaRepository<Plan,Integer> {

    @Query(value = "select * from plan p join client_type ct on ct.id=t.client_type_id where ct.type=:clientType",nativeQuery = true)
    List<Plan> getAllPlansByClientType(String clientType);


}
