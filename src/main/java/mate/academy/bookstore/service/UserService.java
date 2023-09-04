package mate.academy.bookstore.service;

import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.user.register.UserRegistrationRequest;
import mate.academy.bookstore.model.dto.user.register.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequest userRegistrationRequest);

    User findByEmail(String email);
}
