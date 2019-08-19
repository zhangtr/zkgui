package com.teedao.zkgui;


import com.teedao.zkgui.skin.JMetro;
import com.teedao.zkgui.skin.Style;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * The entry point fo the application.
 *
 * @author Frederic Thevenet
 */
public class App extends Application {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        Parent root = loader.load();
        new JMetro(root, Style.LIGHT);
        Scene scene = new Scene(root);
        stage.setTitle("ZkGUI");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/zookeeper_small.gif")));
        stage.show();
    }

}
