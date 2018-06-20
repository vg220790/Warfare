package UI;

import GraphicsContent.GraphicsApplication;
import SharedInterface.WarInterface;
import com.afekawar.bl.base.Entities.BaseEntities.Missile;
import com.afekawar.bl.base.Entities.BaseEntities.MissileDestructor;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncher;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncherDestructor;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.afekawar.bl.base.MainLogic;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class Initialization extends Scene {


     Initialization(VBox root, double width, double height, WarInterface parsedEntities, Stage window, Scene prevScene, SystemTime time) {
        super(root, width, height);


        VBox entitiesView;

        entitiesView = new VBox();
        entitiesView.setSpacing(5);

        entitiesView.getChildren().add(new Label("Missile Launchers: "));
        for (MissileLauncher temp : parsedEntities.getMissileLaunchers()) {
            HBox launcherView = new HBox();
            launcherView.setSpacing(10);
            launcherView.getChildren().add(new Label("Missile Launcher ID: " + temp.getId()));
            launcherView.getChildren().add(new Label("isHidden: " + String.valueOf(temp.getHidden())));
            launcherView.getChildren().add(new Label("Amount Of Missiles: " + String.valueOf(temp.getMissiles().size())));
            VBox missileList = new VBox();
            for (Missile mis : temp.getMissiles()) {
                HBox box = new HBox();
                box.setSpacing(12);
                box.getChildren().add(new Label("Missile ID: " + mis.getId()));
                box.getChildren().add(new Label("Missile Destination Coordinates: " + mis.getTargetCoordinates()));
                box.getChildren().add(new Label("Missile Launch Time: " + mis.getLaunchTime()));
                box.getChildren().add(new Label("Missile Fly Time: " + mis.getFlyTime()));
                box.getChildren().add(new Label("Missile Damage: " + mis.getDamage()));
                missileList.getChildren().add(box);
            }
            launcherView.getChildren().add(missileList);
            entitiesView.getChildren().add(launcherView);

        }
        entitiesView.getChildren().add(new Label(""));
        entitiesView.getChildren().add(new Label("Missile Launcher Destructors: "));
        for (MissileLauncherDestructor temp : parsedEntities.getMissileLauncherDestructors()) {
            HBox destView = new HBox();
            destView.setSpacing(10);
            destView.getChildren().add(new Label("Missile Launcher Destructor Type: " + temp.getType()));
            destView.getChildren().add(new Label("Amount Of Launchers to Destroy: " + String.valueOf(temp.getDestLauncher().size())));
            VBox destroyLauncherList = new VBox();
            /*for (DestLa dest : temp.getDestructedLanucher()) {
                HBox box = new HBox();
                box.setSpacing(12);
                box.getChildren().add(new Label("Launcher ID: " + dest.getId()));
                box.getChildren().add(new Label("Destruct Time: " + dest.getDestructTime()));
                destroyLauncherList.getChildren().add(box);
            }*/
            destView.getChildren().add(destroyLauncherList);
            entitiesView.getChildren().add(destView);
        }
        entitiesView.getChildren().add(new Label(""));
        entitiesView.getChildren().add(new Label("Missile Destructors: "));
        for (MissileDestructor temp : parsedEntities.getMissileDestructors()) {
            HBox destView = new HBox();
            destView.setSpacing(10);
            destView.getChildren().add(new Label("Missile Destructor ID: " + temp.getId()));
            destView.getChildren().add(new Label("Amount Of Missiles to Destroy: " + String.valueOf(temp.getDestructdMissile().size())));
            VBox destroyMissilesList = new VBox();
            /*for (DestMissile dest : temp.getDestructdMissile()) {
                HBox box = new HBox();
                box.setSpacing(12);
                box.getChildren().add(new Label("Missile ID: " + dest.getId()));
                box.getChildren().add(new Label("Destruct After Launch Time: " + dest.getDestructAfterLaunch()));
                destroyMissilesList.getChildren().add(box);
            }*/
            destView.getChildren().add(destroyMissilesList);
            entitiesView.getChildren().add(destView);
        }
        entitiesView.setTranslateX(this.getWidth()/6);
        entitiesView.setTranslateY(this.getHeight()/6);
        entitiesView.setAlignment(Pos.CENTER);

        root.getChildren().add(entitiesView);

         HBox panel = new HBox();

         Button startGame = new Button("Start Simulation");
         startGame.setMinSize(100,50);
         Button goBack = new Button("Go Back");
         goBack.setMinSize(100,50);
         panel.getChildren().add(startGame);
         panel.getChildren().add(goBack);

         panel.setSpacing(50);
         panel.setAlignment(Pos.CENTER);
         panel.setTranslateY(100);

         entitiesView.getChildren().add(panel);


         goBack.setOnAction(event -> window.setScene(prevScene));

         startGame.setOnAction(event ->   {
                 Thread timeThread = new Thread(time);
                 timeThread.start();

                 GraphicsApplication graphicsApplication = new GraphicsApplication(time,parsedEntities);
                 Runnable mainProgram = new MainLogic(time, graphicsApplication,parsedEntities);
                 graphicsApplication.setMainProgram((MainLogic)mainProgram);
                 Thread mainThread = new Thread(mainProgram);
                 mainThread.start();

                 graphicsApplication.start(window);
         });

    }



}
