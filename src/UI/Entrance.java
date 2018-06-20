package UI;


// Max Golotsvan - 314148123
// Maksim Lyashenko - 317914877

import GraphicsContent.GraphicsApplication;
import ConsoleContent.ConsoleApplication;
import SharedInterface.WarImp;
import SharedInterface.WarInterface;
import com.afekawar.bl.base.Interface.Time.MyTime;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.afekawar.bl.base.MainLogic;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Entrance extends Application {
        private Stage window;
        private Scene scene1;
        private Scene scene2;
        private FileChooser fileChooser;
        private SystemTime time;
        private WarInterface parsedEntities;
        public static void main(String[] args) {
            ConsoleApplication consoleApp;
            Scanner scanner = new Scanner(System.in);
            int decision = 0;
            String decisionStr = "";


            while(decision != 1 && decision != 2) {
                while (!isNumeric(decisionStr)) {
                    System.out.println("What kind of implementation would you like to load? 1 - console, 2 - graphical ");
                    decisionStr = scanner.next();
                }
                decision = Integer.parseInt(decisionStr);
                if(decision != 1 && decision != 2)
                    decisionStr = "";
            }

            if(decision == 1) {
                consoleApp = new ConsoleApplication();
                Thread th = new Thread(consoleApp);
                th.start();
            }

            if(decision == 2)
                launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
            this.window = primaryStage;
            time = new MyTime();
            parsedEntities = new WarImp();


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
                    parsedEntities = gson.fromJson(new FileReader(configuration), WarImp.class);



                    scene2 = new Initialization(new VBox(), 1500, 948, parsedEntities, window, scene1, time);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                window.setScene(scene2);
            }

            });
            manualBtn.setOnAction(event -> {
                Thread timeThread = new Thread(time);
                timeThread.start();

                GraphicsApplication graphicsApplication = new GraphicsApplication(time,parsedEntities);
                Runnable mainProgram = new MainLogic(time, graphicsApplication,parsedEntities);
                graphicsApplication.setMainProgram((MainLogic)mainProgram);
                Thread mainThread = new Thread(mainProgram);
                mainThread.start();

                graphicsApplication.start(window);
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

    private static boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    }

