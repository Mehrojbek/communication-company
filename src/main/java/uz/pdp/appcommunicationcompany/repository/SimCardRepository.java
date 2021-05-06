package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.appcommunicationcompany.entity.simcard.SimCard;
import uz.pdp.appcommunicationcompany.payload.GroupByPlan;

import java.sql.Timestamp;
import java.util.*;
import java.util.Optional;
import java.util.UUID;

public interface SimCardRepository extends JpaRepository<SimCard, UUID> {
    boolean existsByNumber(String number);
    boolean existsByNumberAndIdNot(String number, UUID id);
    Optional<SimCard> findByNumber(String number);
    List<SimCard> findAllByBranchId(Integer branch_id);

    List<SimCard> findAllByCreatedAtBetween(Timestamp start, Timestamp end);

    @Query(value = "select plan_id,count(*) from sim_card sc group by plan_id order by count desc",nativeQuery = true)
    List<GroupByPlan> getAllSimCardGroupByPlanIdOrderDesc();
}
