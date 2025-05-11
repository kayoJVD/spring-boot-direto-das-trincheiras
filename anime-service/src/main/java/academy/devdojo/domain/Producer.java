package academy.devdojo.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producer {
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private static final List<Producer> producers = new ArrayList<>();

    static {
        producers.add(Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build());
        producers.add(Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build());
        producers.add(Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build());
    }

    public static List<Producer> producerList() {
        return producers;
    }
}
