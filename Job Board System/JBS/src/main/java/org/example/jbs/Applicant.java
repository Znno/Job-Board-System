package org.example.jbs;

import javafx.beans.property.*;

public class Applicant {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty status;

    public Applicant(int id, String name, String email, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty employer_id_Property() {
        return email;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty stateProperty() {
        return status;
    }
}
