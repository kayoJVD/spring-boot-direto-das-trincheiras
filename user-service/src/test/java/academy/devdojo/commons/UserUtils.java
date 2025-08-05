package academy.devdojo.commons;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserUtils {

    public List<User> newListUsers(){
        User hemerson = User.builder().id(1L).firstName("hemerson").lastName("Alves").email("hemerson@gmail.com").build();
        User leticia = User.builder().id(2L).firstName("Leticia").lastName("Xavier").email("leticia@gmail.com").build();
        User danielle = User.builder().id(3L).firstName("Danielle").lastName("Pereira").email("danielle@gmail.com").build();
        return new ArrayList<>(List.of(hemerson, leticia, danielle));
    }

    public User toSaveNewUser(){
        return User.builder().id(99L).firstName("Alvina").lastName("Pereira").email("alvina@gmail.com").build();
    }

}
