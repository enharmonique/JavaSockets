package app.jdbc;

import app.model.Donation;
import app.repository.DonationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DonationDBRepository implements DonationRepository {
    JdbcUtils jdbcUtils;

    public DonationDBRepository(Properties props) {
        jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public void save(Donation donation) {
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt =
                     con.prepareStatement("insert into donations (donor, charityCase, sum) values (?, ?, ?)")) {
            preStmt.setString(1, donation.getDonor().getName());
            preStmt.setString(2, donation.getCharityCase().getName());
            preStmt.setFloat(3, donation.getSum());
            if (preStmt.executeUpdate() < 1)
                System.err.println("Donation not saved");
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Donation findOne(Integer integer) {
        return null;
    }

    @Override
    public void update(Integer integer, Donation donation) {

    }

    @Override
    public Iterable<Donation> getAll() {
        return null;
    }
}