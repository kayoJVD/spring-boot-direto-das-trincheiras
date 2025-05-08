package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    public List<Anime> animeList = new ArrayList<>();
    public Long idGenarator = 3L;

    @GetMapping
    public List<Anime> listall(@RequestParam(required = false) String name) {

        var animes = Anime.animeList();
        if (name == null) return animes;

        return animes.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    @GetMapping("{id}")
    public Anime findById(@PathVariable Long id) {
        return Anime.animeList().stream().filter(anime -> anime.getId().equals(id))
                .findFirst().orElse(null);
    }


    // Idempotente não altera o valor, há não ser que altera o valor o post abaixo não é Idempotente
    @PostMapping()
    public Anime save(@RequestBody Anime anime) {
        anime.setId(idGenarator++);
        Anime.animeList().add(anime);
        return anime;
    }

}
