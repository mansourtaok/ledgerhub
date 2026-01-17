package com.ledgerhub.repository;

import org.springframework.data.repository.CrudRepository;

import com.ledgerhub.model.db.Country;

public interface CountryRepository extends CrudRepository<Country, Long> {

}
