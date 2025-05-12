package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anime {
    private Long id;
    private String name;
    private static List<Anime> animes = new ArrayList<>();

    static {
        animes.add(new Anime(1L, "HxH"));
        animes.add(new Anime(2L, "One Piece"));
        animes.add(new Anime(3L, "Kimetsu"));
    }

    public static List<Anime> animeList() {
        return animes;
    }
}
