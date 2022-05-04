package net.brightlizard.otus.k8s.repository;

import net.brightlizard.otus.k8s.model.UserEntity;
import net.brightlizard.otus.k8s.openapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@Component
public class UserService {

    @Autowired
    public UserRepository userRepository;

    @PostConstruct
    public void init(){

    }

    public User save(User user){
        UserEntity userEntity = new UserEntity();
        userEntity = convert(user, userEntity);
        userEntity = userRepository.save(userEntity);
        user.setId(userEntity.getId());
        return user;
    }

    public User update(Long userId, User user){
        Optional<UserEntity> userById = userRepository.findById(userId);
        UserEntity userEntity = userById.orElseThrow(() -> new RuntimeException("No such user"));
        userEntity = convert(user, userEntity);
        userEntity = userRepository.save(userEntity);
        user.setId(userEntity.getId());
        return user;
    }

    private UserEntity convert(User user, UserEntity userEntity) {
        userEntity.setUsername(user.getUsername());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());
        return userEntity;
    }

    private User convert2(User user, UserEntity userEntity) {
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setEmail(userEntity.getEmail());
        user.setPhone(userEntity.getPhone());
        return user;
    }

    public User findById(Long userId) {
        Optional<UserEntity> userById = userRepository.findById(userId);
        UserEntity userEntity = userById.orElseThrow(() -> new RuntimeException("No such user"));
        return convert2(new User(), userEntity);
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
