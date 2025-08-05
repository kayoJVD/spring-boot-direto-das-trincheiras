package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.repository.UserHardCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserHardCodeRepository repository;

    public List<User> findAll(String name){
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public User userByIdOrThrowNotFound(Long id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User save(User user){
        return repository.save(user);
    }

    public void delete(Long id){
        User remove = userByIdOrThrowNotFound(id);
        repository.delete(remove);
    }

    public void update(User user){
        userByIdOrThrowNotFound(user.getId());
        repository.update(user);
    }
}
