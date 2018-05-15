package GraphicsContent;

import GraphicsContent.GraphicsEntities.*;
import com.afekawar.bl.base.ConsoleVersion;
import com.afekawar.bl.base.Entities.Missile;
import com.afekawar.bl.base.Entities.MissileDestructor;
import com.afekawar.bl.base.Entities.MissileLauncher;
import com.afekawar.bl.base.Entities.MissileLauncherDestructor;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;

import java.util.Map;

public class WarApplication extends Application {
    private Pane root;
    private Runnable mainProgram;

    private Map<String,GameObject> antiMissiles = new HashMap<>();
    private Map<String,GameObject> antiMissileLaunchers = new HashMap<>();
    private Map<String,GameObject> missiles = new HashMap<>();
    private Map<String,GameObject> missileLaunchers = new HashMap<>();
    private Map<String,GameObject> missileLauncherDestructors = new HashMap<>();
    private Map<String,GameObject> missileDestructors = new HashMap<>();


    private Scene createContent(){
        root = new Pane();
        root.setPrefSize(1500,948);
        root.setId("pane");
        Text time = new Text("");
        time.setStyle("-fx-font: bold 18px \"Serif\"");
        time.setX(10);
        time.setY(20);
        Scene scene = new Scene(root);
        double startTime = System.nanoTime();
        scene.getStylesheets().addAll(this.getClass().getResource("Resources/style.css").toExternalForm());
        root.getChildren().add(time);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
                double currentTime = (System.nanoTime()-startTime)/1000000000;
                currentTime = (int)(currentTime*100);
                currentTime = currentTime/100;
                time.setText(String.valueOf(currentTime));

                for(GameObject missile:missiles.values()){
                    if(missile.isAlive())
                        missile.update();
                    else{
                        missile.getView().setVisible(false);
                        missile.getName().setVisible(false);
                    }
                }

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
        for(Runnable r : ((ConsoleVersion) mainProgram).entities.values()){

            if(r instanceof MissileLauncher){
                if(missileLaunchers.get(((MissileLauncher) r).getId()) == null) {

                    MissileL launcher = new MissileL(((MissileLauncher) r).getId(),((MissileLauncher) r).getCoordinates());
                    if(((MissileLauncher) r).getHidden())
                        launcher.setHidden(true);
                    addMissileLauncher(((MissileLauncher) r).getId(),launcher, ((MissileLauncher) r).getCoordinates().getX() - launcher.getView().getImage().getWidth() / 2, ((MissileLauncher) r).getCoordinates().getY() - launcher.getView().getImage().getHeight() / 2);
                }

                if(!((MissileLauncher) r).getMissiles().isEmpty()) {
                    Missile temp = ((MissileLauncher) r).getMissiles().peek();
                    if(temp != null)
                        if (((MissileLauncher) r).getAlive() && temp.getState() == Missile.State.READY) {

                        temp.setState("INAIR");



                        missileLaunchers.get(((MissileLauncher) r).getId()).setHidden(false);   // Unhide missile launcher when launching a missile

                        double angle = Math.atan2(temp.getTarget().getCoordinates().getY() - ((MissileLauncher) r).getCoordinates().getY(), temp.getTarget().getCoordinates().getX() - ((MissileLauncher) r).getCoordinates().getX()) * 180 / Math.PI +90;



                        MissileInstance missileI = new MissileInstance(temp.getId(),((MissileLauncher) r).getCoordinates(), temp.getTarget().getCoordinates(), temp.getFlyTime());
                        missileI.getView().setRotate(angle);
                        addMissile(temp.getId(), missileI, ((MissileLauncher) r).getCoordinates().getX() - missileI.getView().getImage().getWidth() / 2, ((MissileLauncher) r).getCoordinates().getY() - missileI.getView().getImage().getHeight() / 2);
                        ((MissileLauncher) r).getMissiles().poll();
                    }
                }

                if(!((MissileLauncher) r).getAlive()){
                    missileLaunchers.get(((MissileLauncher) r).getId()).destroy();
                    antiMissileLaunchers.get(((MissileLauncher) r).getId()).setAlive(false);

                }
                if(((MissileLauncher) r).getActiveMissileThread() != null){                                         // Check if missile died and remove it from views.
                    if(!((MissileLauncher) r).getActiveMissileThread().isAlive()){
                        missiles.get(((MissileLauncher) r).getActiveMissileThread().getName()).setAlive(false);
                        if(antiMissiles.get(((MissileLauncher) r).getActiveMissileThread().getName()) != null)
                            antiMissiles.get(((MissileLauncher) r).getActiveMissileThread().getName()).setAlive(false);
                        if(((MissileLauncher) r).getAlive() && !(((MissileLauncher) r).getMissiles().isEmpty()))
                            missileLaunchers.get(((MissileLauncher) r).getId()).setHidden(true);                        // Hide launcher again, once there's no missiles in air
                    }
                }



                }
            else if(r instanceof MissileDestructor){
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
    }

    @Override
    public void start(Stage primaryStage){
        mainProgram = new ConsoleVersion();
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
        addGameObject(missile,x,y);

    }
    private void addAntiMissile(String id, GameObject missile,double x, double y){
        antiMissiles.put(id, missile);
        addGameObject(missile,x,y);

    }
    private void addAntiMissileLauncher(String id, GameObject missile,double x, double y){
        antiMissileLaunchers.put(id, missile);
        addGameObject(missile,x,y);

    }

}


