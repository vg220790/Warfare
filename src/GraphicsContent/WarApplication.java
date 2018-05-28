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

import java.util.HashMap;

import java.util.Map;
import java.util.Stack;

public class WarApplication extends Application implements MissileLauncherListener,MissileLauncherDestructorListener, MissileDestructorListener {
    private Pane root;
    private SystemTime time;

    private Map<String,GameObject> graphicsEntities = new HashMap<>();

    private Stack<GameObject> deadEntities;

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
            public void handle(long now) {
                timeView.setText(String.valueOf(time.getTime()));

                deadEntities = new Stack<>();

                for(GameObject entity : graphicsEntities.values()){
                    if(!root.getChildren().contains(entity.getView()))
                        addGameObject(entity,entity.getCoordinates().getX(),entity.getCoordinates().getY());
                    if(entity.isAlive()) {
                        entity.update();
                        if(entity.isHidden())
                            entity.getView().setOpacity(0.25);
                        else
                            entity.getView().setOpacity(1);
                    }
                    else {
                        deadEntities.push(entity);
                        entity.getView().setVisible(false);
                        entity.getName().setVisible(false);
                    }
                }

                while(!deadEntities.empty()){
                    graphicsEntities.values().remove(deadEntities.pop());
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

        Runnable mainProgram = new ConsoleVersion(time,this);
        Thread mainThread = new Thread(mainProgram);
        mainThread.start();

        primaryStage.setScene(createContent());
        primaryStage.show();

    }
    public static void main(String[] args){
        launch(args);
    }

    private void addGameObject(GameObject object, double x, double y){
            object.getView().setX(x);
            object.getView().setY(y);
            root.getChildren().add(object.getView());
            root.getChildren().add(object.getName());
    }
    @Override
    public synchronized void createMissileLauncher(MissileLauncherEvent e) {
        MissileL launcher = new MissileL(e.getSource().getId(),e.getSource().getCoordinates());
        if(e.getSource().getHidden())
            launcher.setHidden(true);
        graphicsEntities.put(e.getSource().getId(),launcher);
        }

    @Override
    public synchronized void destroyMissileLauncher(MissileLauncherEvent e) {
        if(graphicsEntities.containsKey(e.getSource().getId())) {
           graphicsEntities.get(e.getSource().getId()).destroy();

        }
    }

    @Override
    public synchronized void launchMissile(MissileLauncherEvent e) {
        double angle = Math.atan2(e.getSource().getActiveMissileEntity().getTarget().getCoordinates().getY() - graphicsEntities.get(e.getSource().getId()).getCoordinates().getY(), e.getSource().getActiveMissileEntity().getTarget().getCoordinates().getX() - graphicsEntities.get(e.getSource().getId()).getCoordinates().getX()) * 180 / Math.PI +90;
        if(graphicsEntities.containsKey(e.getSource().getId()))
            graphicsEntities.get(e.getSource().getId()).setHidden(false);

        MissileInstance missile = new MissileInstance(e.getSource().getActiveMissileEntity().getId(),e.getSource().getCoordinates(), e.getSource().getActiveMissileEntity().getTarget().getCoordinates(),e.getSource().getActiveMissileEntity().getFlyTime());
        missile.getView().setRotate(angle);

        graphicsEntities.put(e.getSource().getActiveMissileEntity().getId(), missile);
    }

    @Override
    public synchronized void destroyMissile(MissileLauncherEvent e){
        if(graphicsEntities.containsKey(e.getDestroyedMissileId()))
            graphicsEntities.get(e.getDestroyedMissileId()).destroy();
    }

    @Override
    public synchronized void hideMissileLauncher(MissileLauncherEvent e){
        if(graphicsEntities.containsKey(e.getSource().getId()))
            graphicsEntities.get(e.getSource().getId()).setHidden(true);
    }
    @Override
    public synchronized void launchAntiMissileLauncher(MissileLauncherDestructorEvent e) {
        Point2D collisionPoint = new Point2D(e.getSource().getActiveDestLauncher().getCoordinates().getX(), e.getSource().getActiveDestLauncher().getCoordinates().getY());
        double angle = Math.atan2(collisionPoint.getY() - graphicsEntities.get(e.getSource().getId()).getCoordinates().getY(), collisionPoint.getX() - graphicsEntities.get(e.getSource().getId()).getCoordinates().getX()) * 180 / Math.PI +90;
        AntiMissileLauncherInstance antiMissileLauncherI = new AntiMissileLauncherInstance(e.getSource().getCoordinates().add(new Point2D(graphicsEntities.get(e.getSource().getId()).getView().getTranslateX(), graphicsEntities.get(e.getSource().getId()).getView().getTranslateY())), collisionPoint, e.getSource().getDestructLength());
        antiMissileLauncherI.getView().setRotate(angle);
        graphicsEntities.put("AML" + e.getSource().getActiveDestLauncher().getId(), antiMissileLauncherI);
    }
    @Override
    public synchronized void destroyAntiMissileLauncher(MissileLauncherDestructorEvent e) {
        if(graphicsEntities.containsKey(e.getSource().getActiveDestLauncher().getId()))
            graphicsEntities.get("AML"+e.getSource().getActiveDestLauncher().getId()).destroy();
    }

    @Override
    public synchronized void createMissileLauncherDestructor(MissileLauncherDestructorEvent e) {
        MissileLD launcherDestructor = new MissileLD(e.getSource().getId(), e.getSource().getCoordinates(), e.getSource().getType());
        graphicsEntities.put(e.getSource().getId(), launcherDestructor);
    }

    @Override
    public synchronized void createMissileDestructor(MissileDestructorEvent e) {
        MissileD destructor = new MissileD(e.getSource().getId(), e.getSource().getCoordinates());
        graphicsEntities.put(e.getSource().getId(), destructor);
    }
    @Override
    public synchronized void launchAntiMissile(MissileDestructorEvent e) {
        Point2D collisionPoint = new Point2D(graphicsEntities.get(e.getSource().getActiveDestMissile().getId()).getCoordinates().getX()+ graphicsEntities.get(e.getSource().getActiveDestMissile().getId()).getView().getImage().getWidth()/2 + 60*e.getSource().getDestructLength()* graphicsEntities.get(e.getSource().getActiveDestMissile().getId()).getVelocity().getX(), graphicsEntities.get(e.getSource().getActiveDestMissile().getId()).getCoordinates().getY()+ graphicsEntities.get(e.getSource().getActiveDestMissile().getId()).getView().getImage().getHeight()/2 + 60*e.getSource().getDestructLength()* graphicsEntities.get(e.getSource().getActiveDestMissile().getId()).getVelocity().getY());
        double angle = Math.atan2(collisionPoint.getY() - e.getSource().getCoordinates().getY(), collisionPoint.getX() - e.getSource().getCoordinates().getX()) * 180 / Math.PI +90;
        AntiMissileInstance antiMissileI = new AntiMissileInstance(e.getSource().getCoordinates(), collisionPoint, e.getSource().getDestructLength());
        antiMissileI.getView().setRotate(angle);
        graphicsEntities.put("AM" + e.getSource().getActiveDestMissile().getId(), antiMissileI);
    }
    @Override
    public synchronized void destroyAntiMissile(MissileDestructorEvent e) {
        if(graphicsEntities.containsKey(e.getSource().getActiveDestMissile().getId()))
            graphicsEntities.get("AM" + e.getSource().getActiveDestMissile().getId()).destroy();
    }
}


