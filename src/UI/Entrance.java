package UI;
import UI.JSONParser.WarParser;
import com.afekawar.bl.base.Interface.Time.MyTime;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Entrance extends Application {
        private Stage window;
        private Scene scene1;
        private Scene manualScenario;
        private Scene scene2;
        private FileChooser fileChooser;
        private SystemTime time;
        private VBox root;
        private WarParser parsedEntities;
        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
            this.window = primaryStage;
            time = new MyTime();
            root = new VBox();
            parsedEntities = new WarParser();


            window.setTitle("Afeka War Game");
            fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");

            Button configBtn = new Button();
            Button manualBtn = new Button();
            configBtn.setMinSize(250,70);
            manualBtn.setMinSize(250,70);
            configBtn.setText("Load Scenario from Configuration File");
            manualBtn.setText("Create Scenario Manually");


            configBtn.setOnAction(event -> {
                File configuration = fileChooser.showOpenDialog(primaryStage);
                if(configuration != null)
                {
                Gson gson = new Gson();

                try {
                    parsedEntities = gson.fromJson(new FileReader(configuration), WarParser.class);



                    scene2 = new Initialization(new VBox(), 1500, 948, parsedEntities, window, scene1, time);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                window.setScene(scene2);
            }

            });
            manualBtn.setOnAction(event -> {
                manualScenario = new ManualScenario(new VBox(),1500,948, parsedEntities, window, scene1, time);

                window.setScene(manualScenario);
                    }

            );
            Pane init = new FlowPane();
            scene1 = new Scene(init,1500,948);
            init.setId("pane");
            init.getStylesheets().addAll(this.getClass().getResource("Resources/style.css").toExternalForm());



            VBox container = new VBox();
            container.setMaxWidth(200);
            container.setPrefWidth(200);
            container.setSpacing(10);
            container.setTranslateY(scene1.getHeight()/2 - container.getPrefHeight()/2);
            container.setTranslateX(scene1.getWidth()/2 - container.getPrefWidth()/2);
            container.getChildren().add(configBtn);
            container.getChildren().add(manualBtn);




            init.getChildren().add(container);



            primaryStage.setScene(scene1);
            primaryStage.setResizable(false);

            primaryStage.show();
        }
    }

