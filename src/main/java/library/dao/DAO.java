package library.dao;

public interface DAO {
    public void add(Object obj);
    public void update(Object obj);
    public void delete(Object obj);
    public Object get(Object obj);
    public Object getAll();
}
