package net.brightlizard.otus.k8s.rest;

import net.brightlizard.otus.k8s.openapi.api.UserApi;
import net.brightlizard.otus.k8s.openapi.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    @GetMapping
    public ResponseEntity<User> createUser(@Valid User user) {
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
