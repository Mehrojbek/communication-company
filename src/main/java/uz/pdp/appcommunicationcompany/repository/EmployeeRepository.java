package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.employee.Employee;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByEmailAndEmailCode(String email, String emailCode);
    boolean existsByEmail(String email);
}
