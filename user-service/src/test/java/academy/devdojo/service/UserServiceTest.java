package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.repository.UserRepository;
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
class UserServiceTest {
    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserUtils userUtils;

    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newListUsers();
    }

    @Test
    @DisplayName("findAll returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        List<User> users = service.findAll(null);

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findAll returns a user with name is found")
    @Order(2)
    void findAll_ReturnsUserInList_WhenNameIsFound(){
        User user = userList.getFirst();
        List<User> expected = Collections.singletonList(user);
        BDDMockito.when(repository.findByFirstNameIgnoreCase(user.getFirstName())).thenReturn(expected);

        List<User> usersFound = service.findAll(user.getFirstName());
        Assertions.assertThat(usersFound).containsAll(expected);

    }

    @Test
    @DisplayName("findAll returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound(){
        String name = "not-found";
        BDDMockito.when(repository.findByFirstNameIgnoreCase(name)).thenReturn(Collections.emptyList());
        List<User> user = service.findAll(name);

        Assertions.assertThat(user).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById return user given id")
    @Order(4)
    void findById_ReturnsUserGivenId_WhenSuccessful(){
        User expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        User userById = service.userByIdOrThrowNotFound(expectedUser.getId());

        Assertions.assertThat(userById).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById return ResponseStatusException when id not found")
    @Order(5)
    void findById_ReturnResponseStatusException_WhenIdNotFound(){
        User expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.userByIdOrThrowNotFound(expectedUser.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a user when successful")
    @Order(6)
    void save_CreateAUser_WhenSuccessful(){
        User newUser = userUtils.toSaveNewUser();
        BDDMockito.when(repository.save(newUser)).thenReturn(newUser);
        BDDMockito.when(repository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        User save = service.save(newUser);

        Assertions.assertThat(save).isNotNull().usingRecursiveComparison().isEqualTo(newUser);
        BDDMockito.verify(repository).save(newUser);
    }

    @Test
    @DisplayName("delete remove a user when given id")
    @Order(7)
    void delete_RemoveAUser_WhenSuccessful(){
        User expected = userList.getFirst();
        BDDMockito.when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));

        service.delete(expected.getId());

        BDDMockito.verify(repository).delete(expected);
    }

    @Test
    @DisplayName("delete throws a ResponseStatusException when user not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenUserNotFound(){
        User user = userList.getFirst();
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.delete(user.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update a user")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful(){
        User userToUpdate = userList.getFirst().withFirstName("Caio");


        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));

        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws a ResponseStatusException when user not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenUserNotFound(){
        User expected = userList.getFirst();
        BDDMockito.when(repository.findById(expected.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(expected)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update throws a EmailAlreadyExists when email belongs to another user")
    @Order(11)
    void update_ThrowsEmailAlreadyExists_WhenEmailBelongsToAnotherUser(){
        User savedUser = userList.getLast();
        User userToUpdate = userList.getFirst().withEmail(savedUser.getEmail());
        var email = userToUpdate.getEmail();
        var id = userToUpdate.getId();

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn((Optional.of(savedUser)));

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("save throws a EmailAlreadyExists when email exists")
    @Order(11)
    void save_ThrowsEmailAlreadyExists_WhenEmailAlreadyExists(){
        User savedUser = userList.getLast();
        User UserToSave = userUtils.toSaveNewUser().withEmail(savedUser.getEmail());
        var email = UserToSave.getEmail();


        BDDMockito.when(repository.findByEmail(email)).thenReturn((Optional.of(savedUser)));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(UserToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);

    }
}