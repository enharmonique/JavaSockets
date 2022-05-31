package repo.jdbc;

import app.model.CharityCase;
import org.springframework.stereotype.Repository;
import repo.repository.CharityCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Component
public class CharityCaseDBRepository implements CharityCaseRepository {
    JdbcUtils jdbcUtils;

    public CharityCaseDBRepository() {
    }

    @Autowired
    public CharityCaseDBRepository(Properties props) {
        jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public CharityCase add(CharityCase charityCase) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt =
                     con.prepareStatement("insert into charityCases (name, sum) values (?, ?)")) {
            preStmt.setString(1, charityCase.getName());
            preStmt.setFloat(2, charityCase.getSum());
            if (preStmt.executeUpdate()<1)
                System.err.println("CharityCase not saved");
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
        }
        return charityCase;
    }

    @Override
    public Collection<CharityCase> findAll() {
        Connection con = jdbcUtils.getConnection();
        List<CharityCase> charityCases = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from charityCases ")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    float sum = result.getFloat("sum");
                    CharityCase charityCase = new CharityCase(name, sum);
                    charityCase.setID(id);
                    System.out.println(charityCase);
                    charityCases.add(charityCase);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
        return charityCases;
    }

    @Override
    public CharityCase findByName(String string) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt =
                     con.prepareStatement("select * from charityCases where name = '" + string + "'")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    float sum = result.getFloat("sum");
                    CharityCase charityCase = new CharityCase(name, sum);
                    charityCase.setID(id);
                    return charityCase;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public void save(CharityCase charityCase) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt =
                     con.prepareStatement("insert into charityCases (name, sum) values (?, ?)")) {
            preStmt.setString(1, charityCase.getName());
            preStmt.setFloat(2, charityCase.getSum());
            if (preStmt.executeUpdate()<1)
                System.err.println("CharityCase not saved");
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
        }
    }

    @Override
    public void delete(Integer integer) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt =
                     con.prepareStatement("delete from charityCases where id=" + integer)) {
            if (preStmt.executeUpdate()<1)
                System.err.println("CharityCase not deleted");
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
        }
    }

    @Override
    public CharityCase findOne(Integer integer) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from charityCases where id=" + integer)) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    float sum = result.getFloat("sum");
                    CharityCase charityCase = new CharityCase(name, sum);
                    charityCase.setID(id);
                    System.out.println("charity case found in db:");
                    System.out.println(charityCase);
                    return charityCase;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return null;
    }

    @Override
    public void update(Integer integer, CharityCase charityCase) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update charityCases set name = (?), sum = (?)" +
                "where id=" + integer)) {
            preStmt.setString(1, charityCase.getName());
            preStmt.setFloat(2, charityCase.getSum());
            if (preStmt.executeUpdate() < 1)
                System.err.println("Charity Case not updated");
        } catch (SQLException ex) {
            System.err.println("error DB " + ex);
        }
    }

    @Override
    public Iterable<CharityCase> getAll() {
        Connection con = jdbcUtils.getConnection();
        List<CharityCase> charityCases = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from charityCases")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    float sum = result.getFloat("sum");
                    CharityCase charityCase = new CharityCase(name, sum);
                    charityCase.setID(id);
                    System.out.println(charityCase);
                    charityCases.add(charityCase);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return charityCases;
    }
}
