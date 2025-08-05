package academy.devdojo.repository;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {
    private final List<User> users = new ArrayList<>();

    {
        User kayo = User.builder().id(1L).firstName("Kayo").lastName("Santana").email("kayo@gmail.com").build();
        User kayk = User.builder().id(2L).firstName("Kayk").lastName("Santana").email("kayk@gmail.com").build();
        User cecilia = User.builder().id(3L).firstName("Cecilia").lastName("Alves").email("cecilia@gmail.com").build();
        users.addAll(List.of(kayo, kayk, cecilia));
    }

    public List<User> getUsers() {
        return users;
    }
}
