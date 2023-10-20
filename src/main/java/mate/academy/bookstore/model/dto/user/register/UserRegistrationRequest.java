package mate.academy.bookstore.model.dto.user.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import mate.academy.bookstore.validation.FieldsValueMatch;
import mate.academy.bookstore.validation.ValidEmail;

@Data
@FieldsValueMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
public class UserRegistrationRequest {
    @NotBlank
    @ValidEmail
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 8, max = 40)
    private String password;
    @NotBlank
    @Size(min = 8, max = 40)
    private String repeatPassword;
}
