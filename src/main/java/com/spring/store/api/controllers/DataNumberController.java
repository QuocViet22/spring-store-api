package com.spring.store.api.controllers;

import com.spring.store.api.models.DataNumber;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.repository.DataNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class DataNumberController {
    @Autowired
    DataNumberRepository dataNumberRepository;

    //    get all data numbers
    @GetMapping("/dataNumber")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<DataNumber> getAllDataNumbers() {
        return dataNumberRepository.findAll();
    }

    //    get data number by name
    @GetMapping("/dataNumber/{name}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public DataNumber getDataNumber(@PathVariable("name") String name) {
        return dataNumberRepository.findByName(name);
    }

    //     create voucher
    @PostMapping("/dataNumber")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataNumber> createDataNumber(@RequestBody DataNumber dataNumberRequest) {
        DataNumber dataNumber = new DataNumber();
        dataNumber.setName(dataNumberRequest.getName());
        dataNumber.setValue(dataNumberRequest.getValue());
        dataNumberRepository.save(dataNumber);
        return new ResponseEntity<>(dataNumber, HttpStatus.CREATED);
    }

    //    update dataNumber by name
    @PutMapping("/dataNumber/{name}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateDataNumber(@PathVariable("name") String name, @RequestBody DataNumber dataNumberRequest) {
        DataNumber dataNumber = dataNumberRepository.findByName(name);
        dataNumber.setName(dataNumberRequest.getName());
        dataNumber.setValue(dataNumberRequest.getValue());
        dataNumberRepository.save(dataNumber);
        return ResponseEntity.ok().body(new MessageResponse("Data number has been updated successfully!"));
    }

    //    delete dataNumber by id
    @DeleteMapping("/dataNumber/{id}")
    public ResponseEntity<?> deleteDataNumber(@PathVariable("id") long id) {
        dataNumberRepository.deleteById(id);
        return ResponseEntity.ok().body(new MessageResponse("Data number has been deleted successfully!"));
    }
}
