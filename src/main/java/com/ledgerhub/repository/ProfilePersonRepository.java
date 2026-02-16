package com.ledgerhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ledgerhub.model.db.ProfilePerson;

public interface ProfilePersonRepository extends JpaRepository<ProfilePerson, Long> {

	List<ProfilePerson> findByprofileId(Long profileId);
}
