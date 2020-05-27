package persistence;

public class ExamplePersistentObject extends PersistentObject {
    private int hooligan;

    public ExamplePersistentObject() {
        super();
        hooligan = (int) (Math.random() * 999);
        PersistentObject.save(this);
    }

    public void showNumber() {
        System.out.println("the saved number is " + hooligan);
    }
}