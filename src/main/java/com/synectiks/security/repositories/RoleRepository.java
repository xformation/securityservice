/**
 * 
 */
package com.synectiks.security.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.synectiks.security.entities.Role;

/**
 * @author Rajesh
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, String> {

	public String findIdByName(String name);

}
