package mate.academy.bookstore.model.dto.user.login;

import lombok.Data;
import mate.academy.bookstore.validation.ValidEmail;

@Data
public class UserLoginRequestDto {
    @ValidEmail
    private String email;
    private String password;
}
