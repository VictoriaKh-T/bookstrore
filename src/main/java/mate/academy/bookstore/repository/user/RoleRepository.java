package mate.academy.bookstore.repository.user;

import mate.academy.bookstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long>,
        JpaSpecificationExecutor<Role> {

    @Query("FROM Role r where r.roleName = :role_name ")
    Role getRoleByRoleName(@Param("role_name")Role.RoleName roleName);
}
