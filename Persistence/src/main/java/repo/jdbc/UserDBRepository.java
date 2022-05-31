package repo.jdbc;

import app.model.User;
import repo.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDBRepository implements UserRepository {
    private JdbcUtils jdbcUtils;
    public UserDBRepository(Properties jdbcProps){
        jdbcUtils=new JdbcUtils(jdbcProps);
    }

    @Override
    public User findBy(String string) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt =
                     con.prepareStatement("select * from users where username = '"+string+"'")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username, password);
                    user.setID(id);
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public User findBy(String usernameString, String passwordString) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt =
                     con.prepareStatement("select * from users where username = '"+usernameString+"'"
                     +" and password = '"+passwordString+"'")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username, password);
                    user.setID(id);
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public User findOne(Integer integer) {
        return null;
    }

    @Override
    public void update(Integer integer, User user) {

    }

    @Override
    public Iterable<User> getAll() {
        Connection con = jdbcUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from users")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username, password);
                    user.setID(id);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return users;
    }
}
