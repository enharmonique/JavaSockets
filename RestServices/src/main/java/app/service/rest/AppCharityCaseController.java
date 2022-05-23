package app.service.rest;

import app.model.CharityCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.repository.CharityCaseRepository;
import app.repository.RepositoryException;

import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/app/charityCases")
public class AppCharityCaseController {

    private static final String template = "Hello, %s!";

    @Autowired
    private CharityCaseRepository charityCaseRepository;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format(template, name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public CharityCase[] getAll() {
        Iterable<CharityCase> allCharityCases = charityCaseRepository.getAll();
        Set<CharityCase> result = new TreeSet<CharityCase>();
        System.out.println("Get all charity Cases: ");
        for (CharityCase charityCase : allCharityCases) {
            result.add(charityCase);
            System.out.println("+" + charityCase.getName());
        }
        System.out.println("Size: " + result.size());
        return result.toArray(new CharityCase[result.size()]);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get by id " + id);
        CharityCase charityCase = charityCaseRepository.findOne(id);
        if (charityCase == null)
            return new ResponseEntity<String>("Charity Case not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<CharityCase>(charityCase, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public CharityCase create(@RequestBody CharityCase charityCase) {
        charityCaseRepository.save(charityCase);
        return charityCase;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CharityCase update(@RequestBody CharityCase charityCase) {
        System.out.println("Updating charity case ...");
        charityCaseRepository.update(charityCase.getID(), charityCase);
        return charityCase;
    }

    // @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting charity case ... " + id);
        try {
            charityCaseRepository.delete(id);
            return new ResponseEntity<CharityCase>(HttpStatus.OK);
        } catch (RepositoryException ex) {
            System.out.println("Ctrl Delete charity case exception");
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/{charityCase}/name")
    public String name(@PathVariable String charityCase) {
        CharityCase result = charityCaseRepository.findByName(charityCase);
        System.out.println("Result ..." + result);

        return result.getName();
    }


    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String charityCaseError(RepositoryException e) {
        return e.getMessage();
    }
}
