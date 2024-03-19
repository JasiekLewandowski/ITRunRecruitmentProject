package com.lewandowskijan.itrunrecruitmentproject.controller;

import com.lewandowskijan.itrunrecruitmentproject.dto.PersonDTO;
import com.lewandowskijan.itrunrecruitmentproject.dto.PersonReq;
import com.lewandowskijan.itrunrecruitmentproject.dto.PersonSearchReq;
import com.lewandowskijan.itrunrecruitmentproject.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> findPerson(@RequestBody @Valid PersonSearchReq req) {
        return ResponseEntity.ok(personService.findPerson(req));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPerson(@RequestBody @Valid PersonReq req) {
        personService.createPerson(req);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{personId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePerson(@PathVariable String personId, @RequestBody @Valid PersonReq req) {
        personService.updatePerson(personId, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Boolean> removePerson(@PathVariable String personId) {
        return ResponseEntity.ok(personService.removePerson(personId));
    }

}