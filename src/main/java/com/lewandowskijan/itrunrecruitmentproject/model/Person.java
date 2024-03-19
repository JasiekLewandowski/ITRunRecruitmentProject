package com.lewandowskijan.itrunrecruitmentproject.model;

import com.lewandowskijan.itrunrecruitmentproject.model.validation.ValidPesel;
import com.lewandowskijan.itrunrecruitmentproject.model.validation.ValidPhone;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.pl.PESEL;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.lewandowskijan.itrunrecruitmentproject.model.constraints.PersonConstraints.*;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "person")
public class Person {

    @JacksonXmlProperty(localName = "personId")
    @Size(max = ID_MAX_LENGTH)
    private @NotBlank String personId;

    @JacksonXmlProperty(localName = "firstName")
    @Size(max = FIRST_NAME_MAX_LENGTH)
    private @NotBlank String firstName;

    @JacksonXmlProperty(localName = "lastName")
    @Size(max = LAST_NAME_MAX_LENGTH)
    private @NotBlank String lastName;

    @JacksonXmlProperty(localName = "mobile")
    private @NotBlank @ValidPhone String mobile;

    @JacksonXmlProperty(localName = "email")
    @Size(max = LAST_NAME_MAX_LENGTH)
    private @NotBlank @Email String email;

    @JacksonXmlProperty(localName = "pesel")
    private @NotBlank @ValidPesel String pesel;

}
