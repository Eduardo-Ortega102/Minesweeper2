package Model.abstractInterface;

public interface Observable {

    public void addObserver(Observer oberver);

    public void removeObserver(Observer oberver);

    public void notifyObservers(final String event);
}
