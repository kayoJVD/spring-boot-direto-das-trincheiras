package academy.devdojo.anime.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPutRequest {
    @NotNull(message = "The field 'id' cannot")
    private Long id;
    @NotBlank(message = "The field 'firstName' is required")
    private String firstName;
    @NotBlank(message = "The field 'lastName' is required")
    private String lastName;
    @NotBlank(message = "The 'email' is required")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,10$", message = "The e-mail is not valid")
    private String email;
}
