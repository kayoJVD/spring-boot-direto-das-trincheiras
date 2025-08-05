package academy.devdojo.commons;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> newAnimeList() {
        Anime soloLeveling = new Anime(1L, "Solo Leveling");
        Anime sakamotoDays = new Anime(2L, "Sakamoto Days");
        Anime fireForce = new Anime(3L, "Fire Force");
        return new ArrayList<>(List.of(soloLeveling, sakamotoDays, fireForce));
    }

    public Anime newAnimeToSave() {
        return Anime.builder().id(99L).name("Lazarus").build();
    }
}
