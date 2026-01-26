package academy.devdojo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistsException extends ResponseStatusException {
    public EmailAlreadyExistsException(String mensage) {
        super(HttpStatus.BAD_REQUEST, mensage);
    }
}
