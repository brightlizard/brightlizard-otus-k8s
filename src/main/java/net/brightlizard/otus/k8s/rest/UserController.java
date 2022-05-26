package net.brightlizard.otus.k8s.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import net.brightlizard.otus.k8s.openapi.api.UserApi;
import net.brightlizard.otus.k8s.openapi.model.User;
import net.brightlizard.otus.k8s.repository.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@RestController
@RequestMapping("/")
public class UserController implements UserApi {

    @Autowired
    private UserService userService;
    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter searchedCounter;
    private final Counter deletedCounter;

    public UserController(MeterRegistry meterRegistry) {
        createdCounter = meterRegistry.counter("users.created");
        updatedCounter = meterRegistry.counter("users.updated");
        searchedCounter = meterRegistry.counter("users.searched");
        deletedCounter = meterRegistry.counter("users.deleted");
    }

    @Override
    public ResponseEntity<User> createUser(@Valid User user) {
        user = userService.save(user);
        createdCounter.increment();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateUser(Long userId, @Valid User user) {
        userService.update(userId,user);
        updatedCounter.increment();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> findUserById(Long userId) {
        User userById = userService.findById(userId);
        searchedCounter.increment();
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId) {
        userService.delete(userId);
        deletedCounter.increment();
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
