package uz.pdp.appcommunicationcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcommunicationcompany.entity.Role;
import uz.pdp.appcommunicationcompany.entity.enums.RoleNameEnum;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(RoleNameEnum name);
}
