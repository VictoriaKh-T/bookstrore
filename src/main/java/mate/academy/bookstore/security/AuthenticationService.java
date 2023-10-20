package mate.academy.bookstore.security;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.model.dto.user.login.UserLoginRequestDto;
import mate.academy.bookstore.model.dto.user.login.UserLoginResponseDto;
import mate.academy.bookstore.repository.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                        requestDto.getPassword())
        );
        Set<Role> userRoles = userRepository.findRolesByUsername(requestDto.getEmail());
        String token = jwtUtil.generateToken(authentication.getName(),
                userRoles.stream()
                        .map(Role::getRoleName)
                        .map(Role.RoleName::getAuthority).toList());
        return new UserLoginResponseDto(token);
    }
}
