package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnimePutRequest {
    @NotNull(message = "The field 'id' is not null")
    private Long id;
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
