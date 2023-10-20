package mate.academy.bookstore.service;

import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.user.register.UserRegistrationRequest;
import mate.academy.bookstore.model.dto.user.register.UserResponseDto;
import mate.academy.bookstore.repository.user.RoleRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private  UserServiceImpl userService;

    @Test
    @DisplayName("test method register and return Ok")
    void register_Ok() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("example@com.ua");
        request.setPassword("1234b");
        request.setFirstName("Ben");
        request.setLastName("Lennon");
        request.setRepeatPassword("1234b");

        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.ROLE_USER);
        
        User user = new User();
        user.setDelete(false);
        user.setPassword("hashedPassword");
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRoles(Set.of(userRole));

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(request.getEmail());
        userResponseDto.setId(1L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        String hashedPassword = "hashedPassword";

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn(hashedPassword);
        when(roleRepository.getRoleByRoleName(Role.RoleName.ROLE_USER))
                .thenReturn(userRole);

        when(userRepository.save(user)).thenReturn(user);
        Mockito.lenient().when(userMapper.mapToDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.register(request);

        Assertions.assertEquals(userResponseDto, result);
    }

    @Test
    @DisplayName("test method register and return ReturnRegistrationException")
    void register_ReturnRegistrationException() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("example@com.ua");
        request.setPassword("1234b");
        request.setFirstName("Ben");
        request.setLastName("Lennon");
        request.setRepeatPassword("1234b");

        String messege = "Unable to complete registration";

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        RegistrationException exception = assertThrows(RegistrationException.class, () -> {
            userService.register(request);
        });

        Assertions.assertEquals(messege, exception.getMessage());
        verify(userRepository).findUserByEmail(request.getEmail());
    }

    @Test
    void findByEmail() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.ROLE_USER);

        User user = new User();
        user.setDelete(false);
        user.setPassword("hashedPassword");
        user.setEmail("example@com.ua");
        user.setFirstName("Erik");
        user.setLastName("Remarque");
        user.setRoles(Set.of(userRole));

        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = userService.findByEmail(user.getEmail());

        Assertions.assertEquals(user, result);
    }
}