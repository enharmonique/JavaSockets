package app.model;

public interface Identifiable<ID> {
    void setID(ID id);
    ID getID();
}
