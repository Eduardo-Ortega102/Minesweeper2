package UserInterface.abstractInterface;

public interface ActionFactory<Action> {
    
    public Action createAction(final String action);

}
