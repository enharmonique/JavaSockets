package repo.mock;

import app.model.CharityCase;
import repo.repository.CharityCaseRepository;
import repo.repository.RepositoryException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

//@Component
public class CharityCaseRepositoryMock implements CharityCaseRepository {
    private Map<Integer, CharityCase> allCharityCases;

    public CharityCaseRepositoryMock() {
        allCharityCases = new TreeMap<Integer, CharityCase>();
        populateCharityCases();
    }

    private void populateCharityCases() {
        CharityCase ch1 = new CharityCase("Carbon 180", 900);
        CharityCase ch2 = new CharityCase("First Book", 1200);
        CharityCase ch3 = new CharityCase("Clean Air Task Force", 700);
        CharityCase ch4 = new CharityCase("The Humane League", 90);
        CharityCase ch5 = new CharityCase("GiveDirectly", 890);

        ch1.setID(1);
        ch2.setID(2);
        ch3.setID(3);
        ch4.setID(4);
        ch5.setID(5);

        allCharityCases.put(ch1.getID(), ch1);
        allCharityCases.put(ch2.getID(), ch2);
        allCharityCases.put(ch3.getID(), ch3);
        allCharityCases.put(ch4.getID(), ch4);
        allCharityCases.put(ch5.getID(), ch5);

    }

    @Override
    public CharityCase findByName(String string) {
        for (CharityCase charityCase : getAll()){
            if (Objects.equals(charityCase.getName(), string)) {
                return charityCase;
            }
        }
        return null;
    }

    @Override
    public CharityCase add(CharityCase crrRequest) {
        return null;
    }

    @Override
    public Collection<CharityCase> findAll() {
        return null;
    }

    @Override
    public void save(CharityCase charityCase) {
        charityCase.setID(allCharityCases.size()+1);
        System.out.println("[CharityCaseRepositoryMock] save charity case - entering");
        if (allCharityCases.containsKey(charityCase.getID()))
            throw new RepositoryException("Charity case already exists: " + charityCase.getID());
        allCharityCases.put(charityCase.getID(), charityCase);
        System.out.println("[CharityCaseRepositoryMock] save charity case - exiting ok");
    }

    @Override
    public void delete(Integer integer) {
        if (allCharityCases.containsKey(integer))
            allCharityCases.remove(integer);
        else
            throw new RepositoryException("Charity case with id [" + integer + "] not found for deletion.");
    }

    @Override
    public CharityCase findOne(Integer integer) {
        return allCharityCases.get(integer);
    }

    @Override
    public void update(Integer integer, CharityCase charityCase) {
        if (allCharityCases.containsKey(integer)) {
            if (integer.equals(charityCase.getID())) {
                allCharityCases.put(integer, charityCase);
                return;
            }
        }
        throw new RepositoryException("Charity case could not be updated " + charityCase);
    }

    @Override
    public Iterable<CharityCase> getAll() {
        return allCharityCases.values();
    }
}
