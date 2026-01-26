package academy.devdojo.anime;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.commons.FileUtils;
import academy.devdojo.config.ConnectionConfigurationProperties;
import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = AnimeControllerTest.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo.anime","academy.devdojo.commons"})
@EnableConfigurationProperties(ConnectionConfigurationProperties.class)
class AnimeControllerTest {
    private static final String URL = "/v1/animes";
    @Autowired
    private MockMvc mockMvc;



    @MockBean
    private AnimeRepository repository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private FileUtils fileUtils;

    private List<Anime> animeList;

    @Autowired
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        {
            animeList = animeUtils.newAnimeList();
        }
    }

    @Test
    @DisplayName("GET v1/animes returns a list with all animes when argument is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?=name=Ufotable returns list whit found object when name exists")
    @Order(2)
    void findByName_ReturnsFoundAnimeList_WhenNameIsFound() throws Exception {

        var response = fileUtils.readResourceFile("anime/get-anime-soloLeveling-name-200.json");
        var name = "Solo Leveling";
        var solo = animeList.stream().filter(anime -> anime.getName().equals(name)).findFirst().orElse(null);

        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.singletonList(solo));


        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/anime?name=x returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/1 returns a producer with given id")
    @Order(4)
    void findById_ReturnsProducersById_WhenSuccesful() throws Exception {
        var id = 1L;
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-name-200.json");
        var foundAnime = animeList.stream().filter(anime -> anime.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("(\"GET v1/animes/99 throws NotFound 404 when producer is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception {

        var response = fileUtils.readResourceFile("anime/get-anime-by-id-404.json");
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/animes creates a producer")
    @Order(6)
    void save_CreatesAnime_WhenSuccessful() throws Exception {

        String request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
        String response = fileUtils.readResourceFile("anime/post-response-anime-201.json");

        Anime animeToSave = animeUtils.newAnimeToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @DisplayName("POST v1/animes return Bad request when fields are invalid")
    @Order(7)
    void save_ReturnBadRequest_WhenFieldsInvalid(String fileName, List<String> error) throws Exception {
        String request = fileUtils.readResourceFile("anime/%s".formatted(fileName));


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        Exception resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        String message = resolvedException.getMessage();

        Assertions.assertThat(message).contains(error);
    }

    @Test
    @DisplayName("DELETE v1/animes/99 throws NotFound when anime is not found")
    @Order(8)
    void deleteById_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception {

        var response = fileUtils.readResourceFile("anime/delete-anime-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/")
    @Order(9)
    void update_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception {

        var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");
        var response = fileUtils.readResourceFile("anime/delete-anime-by-id-404.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    private static Stream<Arguments> postAnimeBadRequestSource() {
        var nameRequiredError = "The field 'name is required";
        List<String> error = Collections.singletonList(nameRequiredError);
        return Stream.of(
                Arguments.of("post-request-anime-empty-fields-400.json", error),
                Arguments.of("post-request-anime-blank-fields-400.json", error)
        );
    }

    @ParameterizedTest
    @MethodSource("puttAnimeBadRequestSource")
    @DisplayName("PUT v1/animes return Bad request when fields are invalid")
    @Order(8)
    void update_ReturnBadRequest_WhenFieldsInvalid(String fileName, List<String> error) throws Exception {
        String request = fileUtils.readResourceFile("anime/%s".formatted(fileName));


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        Exception resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        String message = resolvedException.getMessage();

        Assertions.assertThat(message).contains(error);
    }

    private static Stream<Arguments> puttAnimeBadRequestSource() {
        var nameRequiredError = "The field 'name' is required";
        var idRequiredError = "The field 'id' is not null";
        List<String> error = List.of(nameRequiredError, idRequiredError);
        return Stream.of(
                Arguments.of("put-request-anime-empty-fields-400.json", error),
                Arguments.of("put-request-anime-blank-fields-400.json", error)
        );
    }

}