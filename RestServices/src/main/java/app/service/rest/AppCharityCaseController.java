package app.service.rest;

import app.model.CharityCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repo.repository.CharityCaseRepository;
import repo.repository.RepositoryException;

import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping("/app/charityCases")
public class AppCharityCaseController {
    @Autowired
    private CharityCaseRepository crrRepository;

    @GetMapping("/test")
    public String test(@RequestParam(value = "name", defaultValue = "Hello") String name) {
        return name.toUpperCase();
    }

    @PostMapping
    public CharityCase create(@RequestBody CharityCase crrRequest) {
        System.out.println("Creating computerRepairRequest");
        return crrRepository.add(crrRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting charity case ... " + id);
        try {
            crrRepository.delete(id);
            return new ResponseEntity<CharityCase>(HttpStatus.OK);
        } catch (RepositoryException ex) {
            System.out.println("Ctrl Delete charity case exception");
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<CharityCase> getAll() {
        return crrRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get by id " + id);
        CharityCase charityCase = crrRepository.findOne(id);
        System.out.println("CHARITY CASE: "+charityCase);
        if (charityCase == null)
            return new ResponseEntity<String>("Charity case not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<CharityCase>(charityCase, HttpStatus.OK);
    }
}