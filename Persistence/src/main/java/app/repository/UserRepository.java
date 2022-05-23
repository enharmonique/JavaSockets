package app.repository;

import app.model.User;

public interface UserRepository extends ICrudRepository<Integer, User> {
    User findBy(String username);
    User findBy(String username, String password);
}
