package UI;
import GraphicsContent.WarApplication;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Entrance extends Application {
        private FileChooser fileChooser;
        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Afeka War Game");
            fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");

            Button configBtn = new Button();
            Button manualBtn = new Button();
            configBtn.setMinSize(250,70);
            manualBtn.setMinSize(250,70);
            configBtn.setText("Load Scenario from Configuration File");
            manualBtn.setText("Create Scenario Manually");
            configBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    File configuration = fileChooser.showOpenDialog(primaryStage);





                    /*
                   WarApplication war = new WarApplication(configuration);
                   try{
                       Thread.sleep(1000);
                   } catch (InterruptedException e){
                       e.printStackTrace();
                   }

                   war.start(primaryStage);
                    */

                }
            });
            manualBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Hello World!");
                }
            });
            Pane root = new FlowPane();
            Scene window = new Scene(root,1500,948);
            root.setId("pane");
            root.getStylesheets().addAll(this.getClass().getResource("Resources/style.css").toExternalForm());




            VBox container = new VBox();
            container.setMaxWidth(200);
            container.setPrefWidth(200);
            container.setSpacing(10);
            container.setTranslateY(window.getHeight()/2 - container.getPrefHeight()/2);
            container.setTranslateX(window.getWidth()/2 - container.getPrefWidth()/2);
            container.getChildren().add(configBtn);
            container.getChildren().add(manualBtn);
            root.getChildren().add(container);

            primaryStage.setScene(window);
            primaryStage.setResizable(false);

            primaryStage.show();
        }
    }

