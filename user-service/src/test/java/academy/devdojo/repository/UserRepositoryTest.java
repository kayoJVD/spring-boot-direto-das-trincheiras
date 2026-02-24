package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;


@DataJpaTest
@Import(UserUtils.class)
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserUtils userUtils;

    @Test
    @DisplayName("Save")
    @Order(1)
    void save_Creates_WhenSuccessfully() {
        var userToSave = userUtils.toSaveNewUser();

        var savedUser = repository.save(userToSave);

        Assertions.assertThat(savedUser)
                .isNotNull();

        Assertions.assertThat(savedUser.getId())
                .isNotNull();

        Assertions.assertThat(savedUser.getEmail())
                .isEqualTo("alvina@gmail.com");


    }

}