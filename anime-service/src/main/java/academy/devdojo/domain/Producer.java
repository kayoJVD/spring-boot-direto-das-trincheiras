package academy.devdojo.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  Producer {
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


}
