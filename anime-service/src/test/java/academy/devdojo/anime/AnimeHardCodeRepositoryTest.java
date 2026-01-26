package academy.devdojo.anime;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.class)
class AnimeHardCodeRepositoryTest {

    @InjectMocks
    private AnimeHardCodeRepository repository;

    @Mock
    private AnimeData animeData;

    private List<Anime> animeList;

    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {

        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("Return list all animes when success")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenSuccess() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.findAll();

        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findByName return all list whit found object when name exists")
    @Order(2)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var first = animeList.getFirst();

        var animes = repository.findByName(null);

        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns when anime whit id given")
    @Order(3)
    void findById_ReturnsAnimeById_WhenSuccess() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        Anime expectAnime = animeList.getFirst();

        Optional<Anime> animes = repository.findById(expectAnime.getId());

        Assertions.assertThat(animes).isPresent().contains(expectAnime);
    }

    @Test
    @DisplayName("findByName returns list whit found object when name exists")
    @Order(4)
    void findByName_ReturnsFoundAnimeList_WhenNameIsFound() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var expectAnime = animeList.getFirst();

        List<Anime> anime = repository.findByName(expectAnime.getName());
        Assertions.assertThat(anime).contains(expectAnime);
    }

    @Test
    @DisplayName("Save creates an Anime")
    @Order(5)
    void save_CreatesAnime_WhenSuccess() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        Anime berserkSave = animeUtils.newAnimeToSave();

        Anime anime = repository.save(berserkSave);

        Assertions.assertThat(anime).isEqualTo(berserkSave).hasNoNullFieldsOrProperties();

        Optional<Anime> byId = repository.findById(berserkSave.getId());

        Assertions.assertThat(byId).isPresent().contains(berserkSave);
    }

    @Test
    @DisplayName("delete removes an anime")
    @Order(6)
    void delete_RemoveAnime_WhenSuccess() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        Anime animeDelete = animeList.getFirst();

        repository.delete(animeDelete);

        List<Anime> anime = repository.findAll();

        Assertions.assertThat(anime).isNotEmpty().doesNotContain(animeDelete);
    }

    @Test
    @DisplayName("update updates an Anime")
    @Order(7)
    void update_UpdatesAnime_WhenSuccess() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        Anime animeFirst = this.animeList.getFirst();
        animeFirst.setName("Dragon Ball Super");

        repository.update(animeFirst);

        Assertions.assertThat(this.animeList).contains(animeFirst);

        Optional<Anime> animes = repository.findById(animeFirst.getId());
        Assertions.assertThat(animes).isPresent();
        Assertions.assertThat(animes.get().getName()).isEqualTo(animeFirst.getName());
    }
}