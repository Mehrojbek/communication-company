package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.pdp.appcommunicationcompany.entity.simcard.Plan;

import java.util.*;

public interface PlanRepository extends JpaRepository<Plan,Integer> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
}
