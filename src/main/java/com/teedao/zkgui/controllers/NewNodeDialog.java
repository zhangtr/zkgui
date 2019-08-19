package com.teedao.zkgui.controllers;

import com.teedao.zkgui.model.NewNodeRequest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class NewNodeDialog extends Dialog<NewNodeRequest> {
    private static final Logger logger = LogManager.getLogger(NewNodeDialog.class);

    @FXML
    DialogPane newNode;

    public NewNodeDialog() {
    }

    public NewNodeDialog(NewNodeRequest request, Node ownerNode) {
        NewNodeRequest nodeRequest = new NewNodeRequest(request);
        if (ownerNode != null) {
            this.initOwner(ownerNode.getScene().getWindow());
        }
        this.setTitle("New Connection Setting ...");
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/com/teedao/zkgui/NewNodeDialog.fxml"));
            Parent parent = fXMLLoader.load();
            this.setDialogPane((DialogPane) parent);

            TextField nameField = (TextField) parent.lookup("#name");
            TextField valueField = (TextField) parent.lookup("#value");
            ChoiceBox<String> modeField = (ChoiceBox<String>) parent.lookup("#mode");
            nameField.textProperty().bindBidirectional(nodeRequest.nameProperty());
            valueField.textProperty().bindBidirectional(nodeRequest.valueProperty());
            modeField.valueProperty().bindBidirectional(nodeRequest.modeProperty());


            Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);

            okButton.addEventFilter(ActionEvent.ACTION, ae -> {
                try {
                    logger.info("submit..:{}", nodeRequest);
                } catch (Throwable e) {
                    ae.consume();
                }
            });
            okButton.setDisable(true);
            nameField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue.length() ==0) {
                    okButton.setDisable(true);
                }else{
                    okButton.setDisable(false);
                }
            });

            this.setResultConverter(dialogButton -> {
                        ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
                        if (data == ButtonBar.ButtonData.OK_DONE) {
                            return nodeRequest;
                        }
                        return null;
                    }
            );
        } catch (IOException e) {
            logger.error("Failed to load /NewNodeDialog.fxml", e);
        }
    }
}
