package net.brightlizard.otus.k8s.repository;

import net.brightlizard.otus.k8s.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@Component
public interface UserRepository extends CrudRepository<UserEntity, Long> {

}
