package academy.devdojo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class Producer {
    private Long id;
    @JsonProperty("name")
    private String name;
    private static List<Producer> producers = new ArrayList<>();


    public static List<Producer> producerList() {
        var mappa = new Producer(1L, "Mappa");
        var kyotoAnimation = new Producer(2L, "Kyoto Animation");
        var madhouse = new Producer(3L, "Madhouse");
        producers.addAll(List.of(mappa, kyotoAnimation, madhouse));
        return producers;
    }
}
