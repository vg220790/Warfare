package GraphicsContent;
import GraphicsContent.GraphicsEntities.*;
import com.afekawar.bl.base.ConsoleVersion;
import com.afekawar.bl.base.Interface.Communication.*;
import com.afekawar.bl.base.Interface.Time.MyTime;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;

public class WarApplication extends Application implements WarEventListener {
    private Pane root;
    private SystemTime time;
    private File configuration;

    private Map<String,GameObject> graphicsEntities = new HashMap<>();


    public WarApplication(File configuration){
        this.configuration = configuration;
    }

    private Scene createContent(){
        root = new Pane();
        root.setPrefSize(1500,948);
        root.setId("pane");
        Text timeView = new Text("");
        timeView.setStyle("-fx-font: bold 18px \"Serif\"");
        timeView.setX(10);
        timeView.setY(20);
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(this.getClass().getResource("Resources/style.css").toExternalForm());
        root.getChildren().add(timeView);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public synchronized void handle(long now) {
                timeView.setText(String.valueOf(time.getTime()));


                Iterator<GameObject> it = graphicsEntities.values().iterator();

                while(it.hasNext()){
                    GameObject entity = it.next();
                    entity.update();

                    if(!entity.isAlive()){
                        it.remove();
                        break;
                    }

                    if(!root.getChildren().contains(entity.getView()))
                        addGameObject(entity, entity.getCoordinates().getX(), entity.getCoordinates().getY());

                }

            }

        };
        timer.start();
        return scene;
    }
    @Override
    public void start(Stage primaryStage){
        time = new MyTime();
        Thread timeThread = new Thread(time);
        timeThread.start();

        Runnable mainProgram = new ConsoleVersion(time,this,configuration);
        Thread mainThread = new Thread(mainProgram);
        mainThread.start();

        primaryStage.setScene(createContent());
        primaryStage.show();

    }

    private void addGameObject(GameObject object, double x, double y){
            root.getChildren().add(object.getView());
            root.getChildren().add(object.getName());
    }

    @Override
    public synchronized void handleWarEvent(WarEvent e) {
        WarEvent.Event_Type type = e.getEventType();

        double angle;
        Point2D collisionPoint;

        switch (type){

            case CREATE_LAUNCHER:
                MissileL launcher = new MissileL(e.getId(),e.getCoordinates());
                if(e.getHidden())
                    launcher.setHidden(true);
                graphicsEntities.put(e.getId(),launcher);
                break;

            case DESTROY_LAUNCHER:
                if(graphicsEntities.containsKey(e.getId()))
                    graphicsEntities.get(e.getId()).destroy();
                break;

            case LAUNCH_MISSILE:
               if(graphicsEntities.containsKey(e.getId()))
                    graphicsEntities.get(e.getId()).setHidden(false);
                MissileInstance missile = new MissileInstance(e.getMissileId(),e.getCoordinates(), e.getTargetCoordinates());

                graphicsEntities.put(e.getMissileId(), missile);
                break;

            case DESTROY_MISSILE:
                if(graphicsEntities.containsKey(e.getMissileId()))
                    graphicsEntities.get(e.getMissileId()).destroy();
                break;

            case HIDE_LAUNCHER:
                if(graphicsEntities.containsKey(e.getId()))
                    graphicsEntities.get(e.getId()).setHidden(true);
                break;

            case CREATE_MISSILE_LAUNCHER_DESTRUCTOR:
                MissileLD launcherDestructor = new MissileLD(e.getId(), e.getCoordinates(), e.getDestructorType());
                graphicsEntities.put(e.getId(), launcherDestructor);
                break;

            case LAUNCH_ANTI_MISSILE_LAUNCHER:
                AntiMissileLauncherInstance antiMissileLauncher = new AntiMissileLauncherInstance(e.getMissileId(),e.getCoordinates(), e.getTargetCoordinates());
                graphicsEntities.put(e.getMissileId(), antiMissileLauncher);
                break;

            case DESTROY_ANTI_MISSILE_LAUNCHER:
                if(graphicsEntities.containsKey("AML " + e.getTargetLauncherId()))
                    graphicsEntities.get("AML " + e.getTargetLauncherId()).destroy();
                break;

            case CREATE_MISSILE_DESTRUCTOR:
                MissileD destructor = new MissileD(e.getId(), e.getCoordinates());
                graphicsEntities.put(e.getId(), destructor);
                break;

            case LAUNCH_ANTI_MISSILE:
                AntiMissileInstance antiMissile = new AntiMissileInstance(e.getMissileId(),e.getCoordinates(), e.getTargetCoordinates());
                graphicsEntities.put(e.getMissileId(), antiMissile);
                break;

            case DESTROY_ANTI_MISSILE:
                if(graphicsEntities.containsKey("AM " + e.getMissileId()))
                    graphicsEntities.get("AM " + e.getMissileId()).destroy();
                break;

            case UPDATE_COORDINATES:
                if(graphicsEntities.containsKey(e.getId()))
                    graphicsEntities.get(e.getId()).setCoordinates(e.getCoordinates());
                break;
        }
    }
}