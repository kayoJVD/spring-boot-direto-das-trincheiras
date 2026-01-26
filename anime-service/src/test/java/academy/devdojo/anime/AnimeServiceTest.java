package academy.devdojo.anime;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {
    @InjectMocks
    AnimeService service;

    @Mock
    AnimeRepository repository;

    private List<Anime> animeList;

    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {

        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("find all Anime's when name is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        List<Anime> anime = service.findAll(null);

        Assertions.assertThat(anime).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("Returns name when name is found")
    @Order(2)
    void findByName_ReturnsAnime_WhenNameIsFound() {
        Anime anime = animeList.getFirst();
        List<Anime> expectedAnimeFound = Collections.singletonList(anime);
        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(expectedAnimeFound);

        List<Anime> animeFound = service.findAll(anime.getName());

        Assertions.assertThat(animeFound).contains(anime);
    }

    @Test
    @DisplayName("findAll returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        List<Anime> anime = service.findAll(name);
        Assertions.assertThat(anime).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns a anime with given id")
    @Order(4)
    void findById_ReturnsAnimeById_WhenSuccessful() {
        Anime expectAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectAnime.getId())).thenReturn(Optional.of(expectAnime));

        Anime anime = service.animeByIdOrThrowNotFound(expectAnime.getId());

        Assertions.assertThat(anime).isEqualTo(expectAnime);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when anime is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        Anime expectAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectAnime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.animeByIdOrThrowNotFound(expectAnime.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSuccessful() {
        Anime animeSave = animeUtils.newAnimeToSave();
        BDDMockito.when(repository.save(animeSave)).thenReturn(animeSave);

        Anime save = service.save(animeSave);

        Assertions.assertThat(save).isEqualTo(animeSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete anime object when id found")
    @Order(7)
    void delete_AnimeById_WhenIdFound() {
        Anime animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);


        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }

    @Test
    @DisplayName(" delete throw exception ResponseStatusException anime not found")
    @Order(8)
    void delete_throwResponseStatusException_WhenAnime_NotFound() {
        Anime animeToException = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToException.getId())).thenReturn(Optional.empty());


        Assertions.assertThatException().isThrownBy(() -> service.delete(animeToException.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates anime")
    @Order(8)
    void update_UpdatesAnime_WhenSuccessful() {
        Anime animeUpdate = animeList.getFirst();
        animeUpdate.setName("Wind Breaker");

        BDDMockito.when(repository.findById(animeUpdate.getId())).thenReturn(Optional.of(animeUpdate));
        BDDMockito.when(repository.save(animeUpdate)).thenReturn(animeUpdate);

        service.update(animeUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeUpdate));
    }

    @Test
    @DisplayName("throw ResponseStatusException when update fail")
    @Order(9)
    void throwException_WhenUpdate_Fail() {
        Anime animeUpdate = animeList.getFirst();
        animeUpdate.setName("not-found");

        BDDMockito.when(repository.findById(animeUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(animeUpdate)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Assert anime when exist")
    @Order(10)
    void assertAnimeExists_WhenAnimeExist() {
        Anime expectAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectAnime.getId())).thenReturn(Optional.of(expectAnime));

        Assertions.assertThatNoException().isThrownBy(() -> service.assertAnimeExists(expectAnime.getId()));
    }

    @Test
    @DisplayName("throw ResponseStatusException when anime not exist fail")
    @Order(11)
    void throwException_WhenAssertAnimeExists_NotExist() {
        Anime throwAnimeNotExist = animeList.getFirst();
        BDDMockito.when(repository.findById(throwAnimeNotExist.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.assertAnimeExists(throwAnimeNotExist.getId())).isInstanceOf(ResponseStatusException.class);
    }

}