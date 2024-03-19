package com.lewandowskijan.itrunrecruitmentproject.service;

import com.lewandowskijan.itrunrecruitmentproject.dto.PersonDTO;
import com.lewandowskijan.itrunrecruitmentproject.dto.PersonReq;
import com.lewandowskijan.itrunrecruitmentproject.dto.PersonSearchReq;
import com.lewandowskijan.itrunrecruitmentproject.enums.Type;
import com.lewandowskijan.itrunrecruitmentproject.exceptions.GlobalMessages;
import com.lewandowskijan.itrunrecruitmentproject.model.Person;
import com.lewandowskijan.itrunrecruitmentproject.model.mapper.PersonMapper;
import com.lewandowskijan.itrunrecruitmentproject.repository.ExternalPersonRepository;
import com.lewandowskijan.itrunrecruitmentproject.repository.InternalPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final InternalPersonRepository internalPersonRepository;
    private final ExternalPersonRepository externalPersonRepository;
    private final PersonMapper personMapper;

    public PersonDTO findPerson(PersonSearchReq req) {
        if (req.type().equals(Type.INTERNAL)) {
            return personMapper.toDTO(findInternalPerson(req.firstName(), req.lastName(), req.mobile()));
        } else if (req.type().equals(Type.EXTERNAL)) {
            return personMapper.toDTO(findExternalPerson(req.firstName(), req.lastName(), req.mobile()));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GlobalMessages.INVALID_PERSON_TYPE);
    }

    public void createPerson(PersonReq req) {
        if (isPersonDuplicate(req.pesel())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GlobalMessages.PERSON_ALREADY_EXISTS);
        }
        if (req.type().equals(Type.INTERNAL)) {
            saveInternalPerson(req);
        } else if (req.type().equals(Type.EXTERNAL)) {
            saveExternalPerson(req);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GlobalMessages.INVALID_PERSON_TYPE);
        }
    }

    public void updatePerson(String personId, PersonReq req) {
        if (req.type().equals(Type.INTERNAL)) {
            updateInternalPerson(personId, req);
        } else if (req.type().equals(Type.EXTERNAL)) {
            updateExternalPerson(personId, req);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GlobalMessages.INVALID_PERSON_TYPE);
        }
    }

    public boolean removePerson(String personId) {
        if (internalPersonRepository.existsById(personId)) {
            internalPersonRepository.deleteById(personId);
            return true;
        } else if (externalPersonRepository.existsById(personId)) {
            externalPersonRepository.deleteById(personId);
            return true;
        } else {
            return false;
        }
    }

    private Person findInternalPerson(String firstName, String lastName, String mobile) {
        return internalPersonRepository.findByFirstNameAndLastNameAndMobile(firstName, lastName, mobile)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GlobalMessages.PERSON_NOT_FOUND));
    }

    private Person findExternalPerson(String firstName, String lastName, String mobile) {
        return externalPersonRepository.findByFirstNameAndLastNameAndMobile(firstName, lastName, mobile)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GlobalMessages.PERSON_NOT_FOUND));
    }

    private Person findInternalPersonById(String personId) {
        return internalPersonRepository.findByPersonId(personId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GlobalMessages.PERSON_NOT_FOUND));
    }

    private Person findExternalPersonById(String personId) {
        return externalPersonRepository.findByPersonId(personId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GlobalMessages.PERSON_NOT_FOUND));
    }

    private void updateInternalPerson(String personId, PersonReq req) {
        var personToUpdate = findInternalPersonById(personId);
        updatePersonData(personToUpdate, req);
        internalPersonRepository.save(personToUpdate);
    }

    private void updateExternalPerson(String personId, PersonReq req) {
        var personToUpdate = findExternalPersonById(personId);
        updatePersonData(personToUpdate, req);
        externalPersonRepository.save(personToUpdate);
    }

    private void updatePersonData(Person personToUpdate, PersonReq req) {
        personToUpdate.setFirstName(req.firstName());
        personToUpdate.setLastName(req.lastName());
        personToUpdate.setMobile(req.mobile());
        personToUpdate.setEmail(req.email());
        personToUpdate.setPesel(req.pesel());
    }

    private void saveInternalPerson(PersonReq req) {
        var personToSave = buildPerson(req);
        internalPersonRepository.save(personToSave);
    }

    private void saveExternalPerson(PersonReq req) {
        var personToSave = buildPerson(req);
        internalPersonRepository.save(personToSave);
    }

    private boolean isPersonDuplicate(String pesel) {
        return internalPersonRepository.existsByPesel(pesel) || externalPersonRepository.existsByPesel(pesel);
    }

    private Person buildPerson(PersonReq req) {
        var person = new Person();
        person.setPersonId(UUID.randomUUID().toString());
        person.setFirstName(req.firstName());
        person.setLastName(req.lastName());
        person.setEmail(req.email());
        person.setMobile(req.mobile());
        person.setPesel(req.pesel());
        return person;
    }

}
