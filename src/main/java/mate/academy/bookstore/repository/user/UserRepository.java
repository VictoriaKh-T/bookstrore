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
    @Query("FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query("FROM User u JOIN FETCH u.roles WHERE u.id = :userId")
    Optional<User> findById(@Param("userId") Long id);

    @Query("SELECT u.roles FROM User u WHERE u.email = :email")
    Set<Role> findRolesByUsername(@Param("email") String email);
}
