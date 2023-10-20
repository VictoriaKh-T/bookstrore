package mate.academy.bookstore.repository.user;

import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/sql-users/repository/add-user-add-roles.sql"},
executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

@Sql(scripts = {"classpath:database/sql-users/repository/delete-user-dele-roles.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("test method findUserByEmail return User")
    void findUserByEmail_ReturnUser() {
        String email = "user3@example.com";
        Optional<User> result = userRepository.findUserByEmail(email);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(email, result.get().getEmail());

    }

    @Test
    @DisplayName("test method findById return User")
    void findById_ReturnUser() {
        Long userId = 3L;
        Optional<User> result = userRepository.findById(userId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(userId, result.get().getId());
    }

    @Test
    @DisplayName("test method findById return user not found")
    void findById_ReturnNotFound() {
        Long userId = 100L;
        Optional<User> result = userRepository.findById(userId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void findRolesByUsername_ReturnSetOfRoles() {
        String email = "user3@example.com";
        Set<Role> roles = Set.of(roleRepository.getRoleByRoleName(Role.RoleName.ROLE_USER),
                roleRepository.getRoleByRoleName(Role.RoleName.ROLE_ADMIN));
        Assertions.assertFalse(roles.isEmpty());
        Set<Role> result = userRepository.findRolesByUsername(email);
        Assertions.assertEquals(roles, result);
    }
}