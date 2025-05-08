package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Producer {
    private Long id;
    private String name;
    private static List<Producer> animes = new ArrayList<>();


    public static List<Producer> animeList() {
        var hxh = new Producer(1L, "HxH");
        var onePiece = new Producer(2L, "One Piece");
        var kimetsu = new Producer(3L, "Kimetsu");
        animes.addAll(List.of(hxh, onePiece, kimetsu));
        return animes;
    }
}
