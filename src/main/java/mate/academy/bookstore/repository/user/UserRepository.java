package mate.academy.bookstore.repository.user;

import java.util.Optional;
import java.util.Set;
import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {
    @Query("from User u inner join fetch u.roles where u.email = :email")
    Optional<User> findUserByEmail(@Param("email")String email);

    @Query("SELECT u.roles FROM User u WHERE u.email = :email")
    Set<Role> findRolesByUsername(@Param("email") String email);
}
