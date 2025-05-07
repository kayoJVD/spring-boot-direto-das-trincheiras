package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;

    public static List<Anime> animeList (){
        var hxh = new Anime(1L, "HxH");
        var onePiece = new Anime(2L, "One Piece");
        var kimetsu = new Anime(3L, "Kimetsu");
        return List.of(hxh, onePiece, kimetsu);
    }
}
