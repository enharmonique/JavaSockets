package app.jdbc;

import app.model.Donor;
import app.repository.DonorRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonorDBRepository implements DonorRepository {
    JdbcUtils jdbcUtils;
    public DonorDBRepository(Properties props){
        jdbcUtils=new JdbcUtils(props);
    }

    @Override
    public List<Donor> findAllByGivenPattern(String pattern) {
        Connection con = jdbcUtils.getConnection();
        List<Donor> donors = new ArrayList<>();
        try (PreparedStatement preStmt =
                     con.prepareStatement("select * from donors where name like '%"+pattern+"%'")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    String address = result.getString("address");
                    String phone = result.getString("phone");
                    Donor donor = new Donor(name, address, phone);
                    donor.setID(id);
                    donors.add(donor);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return donors;
    }

    @Override
    public Donor findDonor(String donorName, String donorAddress, String donorPhone) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt =
                     con.prepareStatement("select * from donors where name = '"+donorName+"'"
                             +" and address = '"+donorAddress+"'"+"and phone = '"+donorPhone+"'")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    String address = result.getString("address");
                    String phone = result.getString("phone");
                    Donor donor = new Donor(name, address, phone);
                    donor.setID(id);
                    return donor;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public void save(Donor donor) {
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preStmt =
                    con.prepareStatement("insert into donors (name, address, phone) values (?, ?, ?)")){
            preStmt.setString(1, donor.getName());
            preStmt.setString(2, donor.getAddress());
            preStmt.setString(3, donor.getPhone());
            if (preStmt.executeUpdate()<1)
                System.err.println("Donor not saved");
        } catch (SQLException ex) {
            System.err.println("Error DB"+ex);
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Donor findOne(Integer integer) {
        return null;
    }

    @Override
    public void update(Integer integer, Donor donor) {

    }

    @Override
    public Iterable<Donor> getAll() {
        return null;
    }
}
