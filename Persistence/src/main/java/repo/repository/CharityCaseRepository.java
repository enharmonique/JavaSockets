package repo.repository;

import app.model.CharityCase;

import java.util.Collection;

public interface CharityCaseRepository extends ICrudRepository<Integer, CharityCase> {
    CharityCase findByName(String string);

    //rest-services
    CharityCase add(CharityCase crrRequest);
    Collection <CharityCase> findAll();
}
