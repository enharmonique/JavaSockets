package app.repository;

import app.model.CharityCase;

public interface CharityCaseRepository extends ICrudRepository<Integer, CharityCase> {
    CharityCase findByName(String string);
}
