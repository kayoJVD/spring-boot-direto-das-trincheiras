package academy.devdojo.anime.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPostRequest {
    @NotBlank(message = "The field 'firstName' is required")
    private String firstName;
    @NotBlank(message = "The field 'lastName' is required")
    private String lastName;
    @NotBlank(message = "The 'email' is not valid")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,10}$", message = "The e-mail is not valid")
    private String email;
}
