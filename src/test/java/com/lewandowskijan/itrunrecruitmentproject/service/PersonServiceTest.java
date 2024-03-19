package com.lewandowskijan.itrunrecruitmentproject.service;

import com.lewandowskijan.itrunrecruitmentproject.dto.PersonDTO;
import com.lewandowskijan.itrunrecruitmentproject.dto.PersonReq;
import com.lewandowskijan.itrunrecruitmentproject.dto.PersonSearchReq;
import com.lewandowskijan.itrunrecruitmentproject.model.Person;
import com.lewandowskijan.itrunrecruitmentproject.model.mapper.PersonMapper;
import com.lewandowskijan.itrunrecruitmentproject.repository.ExternalPersonRepository;
import com.lewandowskijan.itrunrecruitmentproject.repository.InternalPersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.lewandowskijan.itrunrecruitmentproject.enums.Type.EXTERNAL;
import static com.lewandowskijan.itrunrecruitmentproject.enums.Type.INTERNAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private InternalPersonRepository internalPersonRepository;

    @Mock
    private ExternalPersonRepository externalPersonRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    class FindPerson {
        @Nested
        class WhenOk {
            @Test
            public void findPersonWhenInternal() {
                // Given
                var req = new PersonSearchReq(INTERNAL, "John", "Smith", "+48123456789");

                var internalPerson = new Person();
                when(internalPersonRepository.findByFirstNameAndLastNameAndMobile(anyString(), anyString(), anyString()))
                        .thenReturn(Optional.of(internalPerson));
                when(personMapper.toDTO(internalPerson)).thenReturn(new PersonDTO());

                // When
                var result = personService.findPerson(req);

                // Then
                assertNotNull(result);
                verify(internalPersonRepository, times(1)).findByFirstNameAndLastNameAndMobile("John", "Smith", "+48123456789");
                verify(externalPersonRepository, never()).findByFirstNameAndLastNameAndMobile(anyString(), anyString(), anyString());
                verify(personMapper, times(1)).toDTO(internalPerson);
            }

            @Test
            public void findPersonWhenExternal() {
                // Given
                var req = new PersonSearchReq(EXTERNAL, "John", "Smith", "+48123456789");

                var externalPerson = new Person();
                when(externalPersonRepository.findByFirstNameAndLastNameAndMobile(anyString(), anyString(), anyString()))
                        .thenReturn(Optional.of(externalPerson));
                when(personMapper.toDTO(externalPerson)).thenReturn(new PersonDTO());

                // When
                var result = personService.findPerson(req);

                // Then
                assertNotNull(result);
                verify(externalPersonRepository, times(1)).findByFirstNameAndLastNameAndMobile("John", "Smith", "+48123456789");
                verify(internalPersonRepository, never()).findByFirstNameAndLastNameAndMobile(anyString(), anyString(), anyString());
                verify(personMapper, times(1)).toDTO(externalPerson);
            }
        }

        @Nested
        class WhenException {
            @Test
            public void findPersonWhenInternal_NotFound() {
                // Given
                PersonSearchReq req = new PersonSearchReq(INTERNAL, "John", "Smith", "+48123456789");
                when(internalPersonRepository.findByFirstNameAndLastNameAndMobile("John", "Smith", "+48123456789"))
                        .thenReturn(Optional.empty());

                // Then
                assertThrows(ResponseStatusException.class, () -> personService.findPerson(req));
            }

            @Test
            public void findPersonWhenExternal_NotFound() {
                // Given
                PersonSearchReq req = new PersonSearchReq(EXTERNAL, "John", "Smith", "+48123456789");
                when(externalPersonRepository.findByFirstNameAndLastNameAndMobile("John", "Smith", "+48123456789"))
                        .thenReturn(Optional.empty());

                // Then
                assertThrows(ResponseStatusException.class, () -> personService.findPerson(req));
            }

        }

    }

    @Nested
    class UpdatePerson {
        @Nested
        class WhenOk {

            @Test
            public void updatePersonWhenInternal() {
                // Given
                var personId = "29a8e76e-5e2c-42c4-a013-b4c54cd9aced";
                var personReq = new PersonReq(INTERNAL, "John", "Smith", "+48123456789", "john@example.com", "12345678901");
                var personToUpdate = new Person(INTERNAL.toString(), "Alexander", "Smoth", "+48987654321", "alexander@example.com", "987654321");
                when(internalPersonRepository.findByPersonId(anyString())).thenReturn(Optional.of(personToUpdate));

                // When
                personService.updatePerson(personId, personReq);

                // Then
                verify(internalPersonRepository, times(1)).findByPersonId(personId);
                verify(internalPersonRepository, times(1)).save(personToUpdate);
                assertEquals(personReq.firstName(), personToUpdate.getFirstName());
                assertEquals(personReq.lastName(), personToUpdate.getLastName());
                assertEquals(personReq.mobile(), personToUpdate.getMobile());
                assertEquals(personReq.email(), personToUpdate.getEmail());
                assertEquals(personReq.pesel(), personToUpdate.getPesel());
            }

            @Test
            public void updatePersonWhenExternal() {
                // Given
                var personId = "29a8e76e-5e2c-42c4-a013-b4c54cd9aced";
                var personReq = new PersonReq(EXTERNAL, "John", "Smith", "+48123456789", "john@example.com", "12345678901");
                var personToUpdate = new Person(EXTERNAL.toString(), "Alexander", "Smoth", "+48987654321", "alexander@example.com", "987654321");
                when(internalPersonRepository.findByPersonId(anyString())).thenReturn(Optional.of(personToUpdate));

                // When
                personService.updatePerson(personId, personReq);

                // Then
                verify(internalPersonRepository, times(1)).findByPersonId(personId);
                verify(internalPersonRepository, times(1)).save(personToUpdate);
                assertEquals(personReq.firstName(), personToUpdate.getFirstName());
                assertEquals(personReq.lastName(), personToUpdate.getLastName());
                assertEquals(personReq.mobile(), personToUpdate.getMobile());
                assertEquals(personReq.email(), personToUpdate.getEmail());
                assertEquals(personReq.pesel(), personToUpdate.getPesel());
            }

        }

        @Nested
        class WhenException {
            @Test
            public void updatePersonWhenInternal_PersonNotFound() {
                // Given
                String personId = "29a8e76e-5e2c-42c4-a013-b4c54cd9aced";
                var personReq = new PersonReq(INTERNAL, "John", "Smith", "+48123456789", "john@example.com", "12345678901");
                when(internalPersonRepository.findByPersonId(personId)).thenReturn(Optional.empty());

                // When, Then
                assertThrows(ResponseStatusException.class, () -> personService.updatePerson(personId, personReq));
                verify(internalPersonRepository, times(1)).findByPersonId(personId);
                verifyNoMoreInteractions(internalPersonRepository);
            }

        }

    }
}