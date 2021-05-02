package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.employee.Branch;

public interface BranchRepository extends JpaRepository<Branch,Integer> {
}
