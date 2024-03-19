package com.lewandowskijan.itrunrecruitmentproject.service;

import com.lewandowskijan.itrunrecruitmentproject.dto.PersonDTO;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                var req = new PersonSearchReq(INTERNAL, "John", "Smith", "+48123456789");
                var internalPerson = new Person();

                when(internalPersonRepository.findByFirstNameAndLastNameAndMobile(anyString(), anyString(), anyString()))
                        .thenReturn(Optional.empty());
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

        @Test
        public void findPersonWhenInvalidType() {
            // Given
            PersonSearchReq req = new PersonSearchReq(Type.UNKNOWN, "John", "Doe", "123456789");

            // When/Then
            assertThrows(ResponseStatusException.class, () -> personService.findPerson(req));
        }
    }
}