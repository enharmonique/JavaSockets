package app.donors;

import app.model.Donor;
import app.repository.DonorRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Iterator;
import java.util.List;

public class DonorMain implements DonorRepository {
    public static void main(String[] args) {
        try {
            initialize();
            DonorMain test = new DonorMain();
            Donor donor = new Donor("testDonor", "cairo", "23456789");
            test.save(donor);
            test.getDonors();
            //test.findAllByGivenPattern("a");
            System.out.println();
            test.findDonor("nina", "adresss", "123");
        } catch (Exception e) {
            System.err.println("Exception " + e);
            e.printStackTrace();
        } finally {
            close();
        }
    }

    static SessionFactory sessionFactory;

    public static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    //INSERT
    @Override
    public void save(Donor donor) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(donor);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Error insert" + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void update(Integer integer, Donor donor) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Donor oldDonor = session.get(Donor.class, integer);
                oldDonor.setName(donor.getName());
                oldDonor.setAddress(donor.getAddress());
                oldDonor.setPhone(donor.getPhone());
                session.update(oldDonor);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Error update " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    //DELETE
    @Override
    public void delete(Integer integer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Donor donor = session.get(Donor.class, integer);
                session.delete(donor);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Error delete " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    //SELECT
    public void getDonors() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List donors = session.createQuery("FROM Donor").list();
                for (Iterator iterator = donors.iterator(); iterator.hasNext(); ) {
                    Donor donor = (Donor) iterator.next();
                    System.out.print("Name: " + donor.getName());
                    System.out.print("  Address: " + donor.getAddress());
                    System.out.println("  Phone: " + donor.getPhone());
                }
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Error select " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    public List<Donor> findAllByGivenPattern(String pattern) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List donors = session.createQuery("FROM Donor where name like '%" + pattern + "%'").list();
                tx.commit();
                return donors;
            } catch (RuntimeException ex) {
                System.err.println("Error select " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    @Override
    public Donor findDonor(String donorName, String donorAddress, String donorPhone) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Donor donor = session.createQuery("FROM Donor where name = '" + donorName + "'"
                                + " and address = '" + donorAddress + "'" + "and phone = '" + donorPhone + "'", Donor.class)
                        .setMaxResults(1)
                        .uniqueResult();
                ;
                tx.commit();
                return donor;
            } catch (RuntimeException ex) {
                System.err.println("Error select " + ex);
                if (tx != null)
                    tx.rollback();
            }
            return null;
        }
    }

    @Override
    public Donor findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Donor> getAll() {
        return null;
    }
}
