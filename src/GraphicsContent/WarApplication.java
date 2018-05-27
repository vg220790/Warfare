package GraphicsContent;

import GraphicsContent.GraphicsEntities.*;
import com.afekawar.bl.base.ConsoleVersion;
import com.afekawar.bl.base.Entities.Missile;
import com.afekawar.bl.base.Entities.MissileDestructor;
import com.afekawar.bl.base.Entities.MissileLauncher;
import com.afekawar.bl.base.Entities.MissileLauncherDestructor;
import com.afekawar.bl.base.Interface.Communication.MissileLauncherEvent;
import com.afekawar.bl.base.Interface.Communication.MissileLauncherListener;
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

public class WarApplication extends Application implements MissileLauncherListener{
    private Pane root;
    private Runnable mainProgram;
    private SystemTime time;

    private Map<String,GameObject> antiMissiles = new HashMap<>();
    private Map<String,GameObject> antiMissileLaunchers = new HashMap<>();
    private Map<String,GameObject> missiles = new HashMap<>();
    private Map<String,GameObject> missileLaunchers = new HashMap<>();
    private Map<String,GameObject> missileLauncherDestructors = new HashMap<>();
    private Map<String,GameObject> missileDestructors = new HashMap<>();


    private Stack<String> deadMissiles;

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
             //   onUpdate();
                timeView.setText(String.valueOf(time.getTime()));

                deadMissiles = new Stack<>();
                for(GameObject missile:missiles.values()){

                    if(!root.getChildren().contains(missile.getView()))
                        addGameObject(missile,missile.getCoordinates().getX(),missile.getCoordinates().getY());

                    if(missile.isAlive())
                        missile.update();
                    else{
                        missile.getView().setVisible(false);
                        missile.getName().setVisible(false);
                        deadMissiles.push(missile.getName().toString());
                    }
                }

                while(!deadMissiles.empty())
                    missiles.remove(deadMissiles.pop());

                for(GameObject missileLauncher:missileLaunchers.values()){
                    if(missileLauncher.isHidden()){
                        missileLauncher.getView().setOpacity(0.25);
                    }
                    else{
                        missileLauncher.getView().setOpacity(1);
                    }
                }

                for(GameObject missileLD:missileLauncherDestructors.values()){
                    missileLD.update();
                }
                for(GameObject antiMissile:antiMissiles.values()){
                    if(antiMissile.isAlive())
                        antiMissile.update();
                    else{
                        antiMissile.getView().setVisible(false);
                        antiMissile.getName().setVisible(false);
                    }
                }
                for(GameObject antiMissileLauncher:antiMissileLaunchers.values()){
                    if(antiMissileLauncher.isAlive())
                        antiMissileLauncher.update();
                    else{
                        antiMissileLauncher.getView().setVisible(false);
                        antiMissileLauncher.getName().setVisible(false);
                    }
                }


            }
        };
        timer.start();

        return scene;
    }

    private void onUpdate(){

/*
        for(Runnable r : ((ConsoleVersion) mainProgram).entities.values()){
             if(r instanceof MissileDestructor){
                if(missileDestructors.get(((MissileDestructor) r).getId()) == null) {

                    MissileD destructor = new MissileD(((MissileDestructor) r).getId(), ((MissileDestructor) r).getCoordinates());
                    addMissileDestructor(((MissileDestructor) r).getId(), destructor, ((MissileDestructor) r).getCoordinates().getX() - destructor.getView().getImage().getWidth() / 2, ((MissileDestructor) r).getCoordinates().getY() - destructor.getView().getImage().getHeight() / 2);
                }
                if(((MissileDestructor) r).getActiveDestMissile() != null){
                    Missile targetMissile = ((MissileDestructor) r).getActiveDestMissile();
                    if(missiles.get(targetMissile.getId()) != null) {
                        Point2D collisionPoint = new Point2D(missiles.get(targetMissile.getId()).getCoordinates().getX() + 60*((MissileDestructor) r).getDestructLength()*missiles.get(targetMissile.getId()).getVelocity().getX(), missiles.get(targetMissile.getId()).getCoordinates().getY() + 60*((MissileDestructor) r).getDestructLength()*missiles.get(targetMissile.getId()).getVelocity().getY());



                    double angle = Math.atan2(collisionPoint.getY() - ((MissileDestructor) r).getCoordinates().getY(), collisionPoint.getX() - ((MissileDestructor) r).getCoordinates().getX()) * 180 / Math.PI +90;




                    AntiMissileInstance antiMissileI = new AntiMissileInstance(((MissileDestructor) r).getCoordinates(), collisionPoint, ((MissileDestructor) r).getDestructLength());
                        antiMissileI.getView().setRotate(angle);

                    addAntiMissile(targetMissile.getId(), antiMissileI, ((MissileDestructor) r).getCoordinates().getX() - antiMissileI.getView().getImage().getWidth() / 2, ((MissileDestructor) r).getCoordinates().getY() - antiMissileI.getView().getImage().getHeight() / 2);


                    }
                    ((MissileDestructor) r).setActiveDestMissile(null);
                }
            }
            else if(r instanceof MissileLauncherDestructor){
                if(missileLauncherDestructors.get(((MissileLauncherDestructor) r).getId()) == null) {

                    MissileLD launcherDestructor = new MissileLD(((MissileLauncherDestructor) r).getId(), ((MissileLauncherDestructor) r).getCoordinates(), ((MissileLauncherDestructor) r).getType());
                    addMissileLauncherDestructor(((MissileLauncherDestructor) r).getId(), launcherDestructor, ((MissileLauncherDestructor) r).getCoordinates().getX() - launcherDestructor.getView().getImage().getWidth() / 2, ((MissileLauncherDestructor) r).getCoordinates().getY() - launcherDestructor.getView().getImage().getHeight() / 2);
                }
                if(((MissileLauncherDestructor) r).getActiveDestLauncher() != null){
                    MissileLauncher targetMissileLauncher = ((MissileLauncherDestructor) r).getActiveDestLauncher();
                    if(missileLaunchers.get(targetMissileLauncher.getId()) != null) {
                        Point2D collisionPoint = new Point2D(targetMissileLauncher.getCoordinates().getX(), targetMissileLauncher.getCoordinates().getY());



                        double angle = Math.atan2(collisionPoint.getY() - (missileLauncherDestructors.get(((MissileLauncherDestructor) r).getId()).getCoordinates().getY()), collisionPoint.getX() - (missileLauncherDestructors.get(((MissileLauncherDestructor) r).getId()).getCoordinates().getX())) * 180 / Math.PI +90;




                        AntiMissileLauncherInstance antiMissileLauncherI = new AntiMissileLauncherInstance(missileLauncherDestructors.get(((MissileLauncherDestructor) r).getId()).getCoordinates(), collisionPoint, ((MissileLauncherDestructor) r).getDestructLength());
                        antiMissileLauncherI.getView().setRotate(angle);

                        addAntiMissileLauncher(targetMissileLauncher.getId(), antiMissileLauncherI, missileLauncherDestructors.get(((MissileLauncherDestructor) r).getId()).getCoordinates().getX() - antiMissileLauncherI.getView().getImage().getWidth() / 2, missileLauncherDestructors.get(((MissileLauncherDestructor) r).getId()).getCoordinates().getY() - antiMissileLauncherI.getView().getImage().getHeight() / 2);


                    }
                    ((MissileLauncherDestructor) r).setActiveDestLauncher(null);




                }
                }



        }
*/
    }

    @Override
    public void start(Stage primaryStage){
        time = new MyTime();
        Thread timeThread = new Thread(time);
        timeThread.start();

        mainProgram = new ConsoleVersion(time,this);
        Thread mainThread = new Thread(mainProgram);
        mainThread.start();





        primaryStage.setScene(createContent());
        primaryStage.show();





    }

    public static void main(String[] args){
        System.setProperty("quantum.multithreaded", "false");


        launch(args);
    }

    private synchronized void addGameObject(GameObject object, double x, double y){
            object.getView().setX(x);
            object.getView().setY(y);
            root.getChildren().add(object.getView());
            root.getChildren().add(object.getName());
    }

    private void addMissileLauncher(String id, GameObject missileLauncher,double x, double y){
        missileLaunchers.put(id, missileLauncher);
        addGameObject(missileLauncher,x,y);

    }
    private void addMissileDestructor(String id,GameObject missileDestructor,double x, double y){
        missileDestructors.put(id,missileDestructor);
        addGameObject(missileDestructor,x,y);

    }

    private void addMissileLauncherDestructor(String id, GameObject missileLauncherDestructor,double x, double y){
        missileLauncherDestructors.put(id, missileLauncherDestructor);
        addGameObject(missileLauncherDestructor,x,y);

    }
    private void addMissile(String id, GameObject missile,double x, double y){
        missiles.put(id, missile);


    }
    private void addAntiMissile(String id, GameObject missile,double x, double y){
        antiMissiles.put(id, missile);
        addGameObject(missile,x,y);

    }
    private void addAntiMissileLauncher(String id, GameObject missile,double x, double y){
        antiMissileLaunchers.put(id, missile);
        addGameObject(missile,x,y);

    }

    @Override
    public void launcherCreated(MissileLauncherEvent e) {
        MissileL launcher = new MissileL(e.getSource().getId(),e.getSource().getCoordinates());
        if(e.getSource().getHidden())
            launcher.setHidden(true);
        addMissileLauncher(e.getSource().getId(),launcher, e.getSource().getCoordinates().getX() - launcher.getView().getImage().getWidth() / 2, e.getSource().getCoordinates().getY() - launcher.getView().getImage().getHeight() / 2);

    }

    @Override
    public void launcherDestroyed(MissileLauncherEvent e) {

    }

    @Override
    public void launchMissile(MissileLauncherEvent e) {
        double angle = Math.atan2(e.getSource().getActiveMissileEntity().getTarget().getCoordinates().getY() - e.getSource().getCoordinates().getY(), e.getSource().getActiveMissileEntity().getTarget().getCoordinates().getX() - e.getSource().getCoordinates().getX()) * 180 / Math.PI +90;

        if(missileLaunchers.containsKey(e.getSource().getId()))
            missileLaunchers.get(e.getSource().getId()).setHidden(false);

        MissileInstance missile = new MissileInstance(e.getSource().getActiveMissileEntity().getId(),e.getSource().getCoordinates(), e.getSource().getActiveMissileEntity().getTarget().getCoordinates(),e.getSource().getActiveMissileEntity().getFlyTime());
        missile.getView().setRotate(angle);


        addMissile(e.getSource().getActiveMissileEntity().getId(), missile, e.getSource().getCoordinates().getX() - missile.getView().getImage().getWidth() / 2, e.getSource().getCoordinates().getY() - missile.getView().getImage().getHeight() / 2);

    }

    @Override
    public void destroyMissile(MissileLauncherEvent e){
        if(missiles.containsKey(e.getDestroyedMissileId()))
            missiles.get(e.getDestroyedMissileId()).setAlive(false);

    }

    @Override
    public void hideMissileLauncher(MissileLauncherEvent e){
        if(missileLaunchers.containsKey(e.getSource().getId()))
            missileLaunchers.get(e.getSource().getId()).setHidden(true);
    }
}


