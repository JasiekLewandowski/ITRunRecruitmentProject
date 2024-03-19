package com.lewandowskijan.itrunrecruitmentproject.model.mapper;

import com.lewandowskijan.itrunrecruitmentproject.dto.PersonDTO;
import com.lewandowskijan.itrunrecruitmentproject.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(source = "personId", target = "personId")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "mobile", target = "mobile")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "pesel", target = "pesel")
    PersonDTO toDTO(Person person);

}
