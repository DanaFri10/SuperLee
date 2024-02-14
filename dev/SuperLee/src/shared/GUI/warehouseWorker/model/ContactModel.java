package shared.GUI.warehouseWorker.model;

public class ContactModel {
    private String id;
    private String name;
    private String email;

    public ContactModel(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name + " via " + email;
    }
}
