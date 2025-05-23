package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

     {

         Anime hxh = new Anime(1L, "HxH");
         Anime onePiece = new Anime(2L, "One Piece");
         Anime kimetsu = new Anime(3L, "Kimetsu");
         animes.addAll(List.of(hxh, onePiece, kimetsu));
     }


    public List<Anime> getAnimes() {
        return animes;
    }
}
