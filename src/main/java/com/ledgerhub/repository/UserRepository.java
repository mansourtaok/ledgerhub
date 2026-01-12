package com.ledgerhub.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ledgerhub.model.db.User;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("select u from User u where u.email = :email")
	User findUserByEmail(@Param("email") String email);

	boolean existsByEmail(String email);

}
