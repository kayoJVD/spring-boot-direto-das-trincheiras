package academy.devdojo.request;

import lombok.Data;

@Data
public class UserPostRequest {
    private String firstName;
    private String lastName;
    private String email;
}
