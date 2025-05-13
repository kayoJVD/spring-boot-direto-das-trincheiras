package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodeRepository {
    private static final  List<Anime> ANIMES = new ArrayList<>();

    static {
        ANIMES.add(new Anime(1L, "HxH"));
        ANIMES.add(new Anime(2L, "One Piece"));
        ANIMES.add(new Anime(3L, "Kimetsu"));
    }

    public static List<Anime> animeList() {
        return ANIMES;
    }

    public List<Anime> findAll(){
        return ANIMES;
    }

    public List<Anime> findByName(String name){
        return ANIMES.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    public Optional<Anime> findById(Long id){
        return ANIMES.stream().filter(anime -> anime.getId().equals(id)).findFirst();
    }

    public Anime save(Anime anime){
         ANIMES.add(anime);
         return anime;
    }
    public void delete(Anime anime){
        ANIMES.remove(anime);
    }

    public void update(Anime anime){
        delete(anime);
        save(anime);
    }
}
