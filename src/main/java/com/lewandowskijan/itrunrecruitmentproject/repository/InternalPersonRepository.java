package com.lewandowskijan.itrunrecruitmentproject.repository;

import com.lewandowskijan.itrunrecruitmentproject.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InternalPersonRepository extends MongoRepository<Person, String> {

    Optional<Person> findByFirstNameAndLastNameAndMobile(String firstName, String lastName, String mobile);
    Optional<Person> findByPersonId(String personId);
    boolean existsByPesel(String pesel);

}
