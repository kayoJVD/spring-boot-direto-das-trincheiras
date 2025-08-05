package academy.devdojo.controller;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.repository.UserHardCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@WebMvcTest
//@Import({UserMapper.class, UserService.class, UserHardCodeRepository.class})
@ComponentScan(basePackages = "academy.devdojo")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserData userData;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private ResourceLoader resourceLoader;
    @MockitoSpyBean
    private UserHardCodeRepository repository;

    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newListUsers();
    }

    @Test
    @DisplayName("GET/v1/users returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var response = readFile("user/get-users-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?name=hemerson returns a user with name is found")
    @Order(2)
    void findAll_ReturnsUserInList_WhenNameIsFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var response = readFile("user/get-users-param-name-200.json");

        String firstName = "hemerson";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("name", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("Get v1/users?name=not-found returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        String response = readFile("user/get-users-param-name-notFound-200.json");
        var name = "not-found";


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users{id} return user given id")
    @Order(4)
    void findById_ReturnsUserGivenId_WhenSuccessful() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        String response = readFile("user/get-users-by-id-200.json");
        var id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users{id} return ResponseStatusException when id not found")
    @Order(5)
    void findById_ReturnResponseStatusException_WhenIdNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Post v1/users save create a User")
    @Order(6)
    void save_CreatedUser_WhenSuccessful() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        String request = readFile("user/post-request-user-200.json");
        String response = readFile("user/post-response-user-201.json");
        User.UserBuilder userToSave = User.builder().id(99L).firstName("Caio").lastName("Leonardo").email("caio@gmail.com");

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave.build());

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("Put v1/users update a user")
    @Order(7)
    void update_UpdateUser_WhenSuccessful() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        String request = readFile("user/put-request-user-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Put v1/users return 404 when id not found")
    @Order(8)
    void update_ThrowResponseStatusException_WhenNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        String request = readFile("user/put-request-user-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));
    }

    @Test
    @DisplayName("Delete v1/user/1 remove a user when id found")
    @Order(9)
    void delete_RemoveUser_WhenSuccessful() throws Exception {
        User id = userList.getFirst();
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", id.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Delete v1/user/99 throw a 404 when id not found")
    @Order(10)
    void delete_ThrowResponseStatusException_WhenNotFound() throws Exception {
        var id = 99L;
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));
    }


    private String readFile(String fileName) throws IOException {
        File file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}