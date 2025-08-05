package academy.devdojo.request;

import lombok.Data;

@Data
public class UserPutRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
