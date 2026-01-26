package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(name);
    }

    public User userByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User user) {
        assertEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }

    public void delete(Long id) {
        User remove = userByIdOrThrowNotFound(id);
        repository.delete(remove);
    }

    public void update(User user) {
        assertUserExists(user.getId());
        assertEmailDoesNotExist(user.getEmail(), user.getId());
        repository.save(user);
    }

    public void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email)
                .ifPresent(this::throwEmailExistsException);
    }

    public void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id)
                .ifPresent(this::throwEmailExistsException);
    }

    public void assertUserExists(Long id) {
        userByIdOrThrowNotFound(id);
    }

    private void throwEmailExistsException(User user) {
        throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(user.getEmail()));
    }
}
