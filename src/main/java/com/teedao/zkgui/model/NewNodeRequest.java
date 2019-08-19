package com.teedao.zkgui.model;

import com.teedao.zkgui.asyncTree.ZooPath;
import javafx.beans.property.SimpleStringProperty;

public class NewNodeRequest {

    private ZooPath parent;
    private SimpleStringProperty name;
    private SimpleStringProperty value;
    private SimpleStringProperty mode;

    public NewNodeRequest() {
        this(new SimpleStringProperty(), new SimpleStringProperty(), new SimpleStringProperty());
    }

    public NewNodeRequest(NewNodeRequest request) {
        this(request.nameProperty(), request.valueProperty(), request.modeProperty());
        this.parent = request.getParent();
    }

    public NewNodeRequest(SimpleStringProperty name, SimpleStringProperty value, SimpleStringProperty mode) {
        this.name = name;
        this.value = value;
        this.mode = mode;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public String getMode() {
        return mode.get();
    }

    public SimpleStringProperty modeProperty() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode.set(mode);
    }

    public ZooPath getParent() {
        return parent;
    }

    public void setParent(ZooPath parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "NewNodeRequest{" +
                "parent=" + parent +
                ", name=" + name +
                ", value=" + value +
                ", mode=" + mode +
                '}';
    }
}
