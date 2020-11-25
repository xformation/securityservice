/**
 * 
 */
package com.synectiks.security.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.synectiks.security.entities.User;
import com.synectiks.security.interfaces.IUserRepository;

/**
 * @author Rajesh
 */
@Repository
public interface UserRepository extends CrudRepository<User, String>,
		IUserRepository {

	Optional<User> findById(long id);
	User findByUsername(String username);

}
