package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
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

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserHardCodeRepositoryTest {
    @InjectMocks
    private UserHardCodeRepository repository;

    @Mock
    private UserData userData;

    @InjectMocks
    private UserUtils userUtils;

    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newListUsers();
    }

    @Test
    @DisplayName("findAll returns a list whit all users")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findByName returns a user when name is found")
    @Order(2)
    void findByName_ReturnUserName_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        User expected = userList.getFirst();

        var byName = repository.findByName(expected.getFirstName());

        Assertions.assertThat(byName).contains(expected);
    }

    @Test
    @DisplayName("findByName returns empty list of user when name is null")
    @Order(3)
    void findByName_ReturnUserEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findByName(null);

        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns user whit given Id")
    @Order(4)
    void findById_ReturnUserById_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var expected = userList.getFirst();

        Optional<User> byId = repository.findById(expected.getId());

        Assertions.assertThat(byId).isPresent().contains(expected);
    }

    @Test
    @DisplayName("Create User when successful")
    @Order(5)
    void save_CreatesUser_WhenSuccessful(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var toSave = userUtils.toSaveNewUser();

        User save = repository.save(toSave);

        Assertions.assertThat(save).isEqualTo(toSave).hasNoNullFieldsOrProperties().isNotNull();

        Optional<User> userToSaveOptional = repository.findById(toSave.getId());

        Assertions.assertThat(userToSaveOptional).isPresent().contains(toSave);
    }

    @Test
    @DisplayName("Delete remove a user ")
    @Order(6)
    void delete_DeleteUser_WhenSuccessful(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        User UserToDelete = userList.getFirst();

        repository.delete(UserToDelete);

        List<User> users = repository.findAll();
        Assertions.assertThat(users).doesNotContain(UserToDelete);
    }

    @Test
    @DisplayName("Update a user")
    @Order(7)
    void update_UpdateUser_WhenSuccessful(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        User update = this.userList.getFirst();
        update.setFirstName("Lucas");

        repository.update(update);

        Optional<User> byId = repository.findById(update.getId());

        Assertions.assertThat(this.userList).contains(update);
        Assertions.assertThat(byId.get().getFirstName()).isEqualTo(update.getFirstName());
    }

}