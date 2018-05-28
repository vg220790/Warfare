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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;

import java.util.Map;
import java.util.Stack;

public class WarApplication extends Application implements MissileLauncherListener,MissileLauncherDestructorListener, MissileDestructorListener {
    private Pane root;
    private Runnable mainProgram;
    private SystemTime time;

    /*
    private Map<String,GameObject> antiMissiles = new HashMap<>();
    private Map<String,GameObject> antiMissileLaunchers = new HashMap<>();
    private Map<String,GameObject> missiles = new HashMap<>();
    private Map<String,GameObject> missileLaunchers = new HashMap<>();
    private Map<String,GameObject> missileLauncherDestructors = new HashMap<>();
    private Map<String,GameObject> missileDestructors = new HashMap<>();
*/
    private Map<String,GameObject> ents = new HashMap<>();

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
                timeView.setText(String.valueOf(time.getTime()));



                for(GameObject ent : ents.values()){
                    if(!root.getChildren().contains(ent.getView()))
                        addGameObject(ent,ent.getCoordinates().getX(),ent.getCoordinates().getY());
                    if(ent.isAlive())
                        ent.update();
                    else {
                        ent.getView().setVisible(false);
                        ent.getName().setVisible(false);
                    }
                }



                /*
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
                    if(!root.getChildren().contains(missileLauncher.getView()))
                        addGameObject(missileLauncher,missileLauncher.getCoordinates().getX(),missileLauncher.getCoordinates().getY());
                    if(missileLauncher.isHidden()){
                        missileLauncher.getView().setOpacity(0.25);
                    }
                    else{
                        missileLauncher.getView().setOpacity(1);
                    }
                }

                for(GameObject missileLD:missileLauncherDestructors.values()){
                    if(!root.getChildren().contains(missileLD.getView()))
                        addGameObject(missileLD,missileLD.getCoordinates().getX(),missileLD.getCoordinates().getY());
                    missileLD.update();
                }
                for(GameObject antiMissile:antiMissiles.values()){
                    if(!root.getChildren().contains(antiMissile.getView()))
                        addGameObject(antiMissile,antiMissile.getCoordinates().getX()+antiMissile.getView().getImage().getWidth()/2,antiMissile.getCoordinates().getY() + antiMissile.getView().getImage().getHeight()/2);
                    if(antiMissile.isAlive())
                        antiMissile.update();
                    else{
                        antiMissile.getView().setVisible(false);
                        antiMissile.getName().setVisible(false);
                    }
                }
                for(GameObject antiMissileLauncher:antiMissileLaunchers.values()){
                    if(!root.getChildren().contains(antiMissileLauncher.getView()))
                        addGameObject(antiMissileLauncher,antiMissileLauncher.getCoordinates().getX(),antiMissileLauncher.getCoordinates().getY());

                    if(antiMissileLauncher.isAlive())
                        antiMissileLauncher.update();
                    else{
                        antiMissileLauncher.getView().setVisible(false);
                        antiMissileLauncher.getName().setVisible(false);
                    }
                }
                for(GameObject missileD : missileDestructors.values()){
                    if(!root.getChildren().contains(missileD.getView()))
                        addGameObject(missileD,missileD.getCoordinates().getX(),missileD.getCoordinates().getY());
                }

                */
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

    private void addGameObject(GameObject object, double x, double y){
            object.getView().setX(x);
            object.getView().setY(y);
            root.getChildren().add(object.getView());
            root.getChildren().add(object.getName());
    }

    private void addMissileLauncher(String id, GameObject missileLauncher){
        //missileLaunchers.put(id, missileLauncher);
        ents.put(id,missileLauncher);

    }
    private void addMissileDestructor(String id,GameObject missileDestructor){
       // missileDestructors.put(id,missileDestructor);
        ents.put(id,missileDestructor);
    }

    private void addMissileLauncherDestructor(String id, GameObject missileLauncherDestructor){
       // missileLauncherDestructors.put(id, missileLauncherDestructor);
        ents.put(id,missileLauncherDestructor);

    }
    private void addMissile(String id, GameObject missile){
        //missiles.put(id, missile);
        ents.put(id,missile);


    }
    private void addAntiMissile(String id, GameObject missile){
        //antiMissiles.put(id, missile);
        ents.put("AM" + id,missile);

    }
    private void addAntiMissileLauncher(String id, GameObject missile){
        //antiMissileLaunchers.put(id, missile);
        ents.put("AML"+id,missile);


    }

    @Override
    public synchronized void createMissileLauncher(MissileLauncherEvent e) {
        MissileL launcher = new MissileL(e.getSource().getId(),e.getSource().getCoordinates());
        if(e.getSource().getHidden())
            launcher.setHidden(true);
        addMissileLauncher(e.getSource().getId(),launcher);
        root.getChildren().add(new Circle(e.getSource().getCoordinates().getX(),e.getSource().getCoordinates().getY(),10));
    }

    @Override
    public synchronized void destroyMissileLauncher(MissileLauncherEvent e) {
        //if(missileLaunchers.containsKey(e.getSource().getId()))
            //missileLaunchers.get(e.getSource().getId()).destroy();
        if(ents.containsKey(e.getSource().getId())) {
           ents.get(e.getSource().getId()).destroy();

        }
    }

    @Override
    public synchronized void launchMissile(MissileLauncherEvent e) {
        double angle = Math.atan2(e.getSource().getActiveMissileEntity().getTarget().getCoordinates().getY() - e.getSource().getCoordinates().getY(), e.getSource().getActiveMissileEntity().getTarget().getCoordinates().getX() - e.getSource().getCoordinates().getX()) * 180 / Math.PI +90;

      //  if(missileLaunchers.containsKey(e.getSource().getId()))
           // missileLaunchers.get(e.getSource().getId()).setHidden(false);
        if(ents.containsKey(e.getSource().getId()))
            ents.get(e.getSource().getId()).setHidden(false);

        MissileInstance missile = new MissileInstance(e.getSource().getActiveMissileEntity().getId(),e.getSource().getCoordinates(), e.getSource().getActiveMissileEntity().getTarget().getCoordinates(),e.getSource().getActiveMissileEntity().getFlyTime());
        missile.getView().setRotate(angle);


        addMissile(e.getSource().getActiveMissileEntity().getId(), missile);

    }

    @Override
    public synchronized void destroyMissile(MissileLauncherEvent e){
       // if(missiles.containsKey(e.getDestroyedMissileId()))
           // missiles.get(e.getDestroyedMissileId()).setAlive(false);
        if(ents.containsKey(e.getDestroyedMissileId()))
            ents.get(e.getDestroyedMissileId()).setAlive(false);

    }

    @Override
    public synchronized void hideMissileLauncher(MissileLauncherEvent e){
       // if(missileLaunchers.containsKey(e.getSource().getId()))
         //   missileLaunchers.get(e.getSource().getId()).setHidden(true);
        if(ents.containsKey(e.getSource().getId()))
            ents.get(e.getSource().getId()).setHidden(true);
    }

    @Override
    public synchronized void launchAntiMissileLauncher(MissileLauncherDestructorEvent e) {
        Point2D collisionPoint = new Point2D(e.getSource().getActiveDestLauncher().getCoordinates().getX(), e.getSource().getActiveDestLauncher().getCoordinates().getY());



        //double angle = Math.atan2(collisionPoint.getY() - missileLauncherDestructors.get(e.getSource().getId()).getCoordinates().getY(), collisionPoint.getX() - missileLauncherDestructors.get(e.getSource().getId()).getCoordinates().getX()) * 180 / Math.PI +90;
        double angle = Math.atan2(collisionPoint.getY() - ents.get(e.getSource().getId()).getCoordinates().getY(), collisionPoint.getX() - ents.get(e.getSource().getId()).getCoordinates().getX()) * 180 / Math.PI +90;




        //AntiMissileLauncherInstance antiMissileLauncherI = new AntiMissileLauncherInstance(e.getSource().getCoordinates().add(new Point2D(missileLauncherDestructors.get(e.getSource().getId()).getView().getTranslateX(),missileLauncherDestructors.get(e.getSource().getId()).getView().getTranslateY())), collisionPoint, e.getSource().getDestructLength());
        AntiMissileLauncherInstance antiMissileLauncherI = new AntiMissileLauncherInstance(e.getSource().getCoordinates().add(new Point2D(ents.get(e.getSource().getId()).getView().getTranslateX(),ents.get(e.getSource().getId()).getView().getTranslateY())), collisionPoint, e.getSource().getDestructLength());

        antiMissileLauncherI.getView().setRotate(angle);

        addAntiMissileLauncher(e.getSource().getActiveDestLauncher().getId(), antiMissileLauncherI);



    }

    @Override
    public synchronized void destroyAntiMissileLauncher(MissileLauncherDestructorEvent e) {
      //  if(antiMissileLaunchers.containsKey(e.getSource().getActiveDestLauncher().getId()))
          //  antiMissileLaunchers.get(e.getSource().getActiveDestLauncher().getId()).setAlive(false);
        if(ents.containsKey(e.getSource().getActiveDestLauncher().getId()))
            ents.get("AML"+e.getSource().getActiveDestLauncher().getId()).setAlive(false);
    }

    @Override
    public synchronized void createMissileLauncherDestructor(MissileLauncherDestructorEvent e) {
        MissileLD launcherDestructor = new MissileLD(e.getSource().getId(), e.getSource().getCoordinates(), e.getSource().getType());
        addMissileLauncherDestructor(e.getSource().getId(), launcherDestructor);
    }

    @Override
    public synchronized void createMissileDestructor(MissileDestructorEvent e) {
        MissileD destructor = new MissileD(e.getSource().getId(), e.getSource().getCoordinates());
        addMissileDestructor(e.getSource().getId(), destructor);

    }

    @Override
    public synchronized void launchAntiMissile(MissileDestructorEvent e) {
       // Point2D collisionPoint = new Point2D(missiles.get(e.getSource().getActiveDestMissile().getId()).getCoordinates().getX() + 60*e.getSource().getDestructLength()*missiles.get(e.getSource().getActiveDestMissile().getId()).getVelocity().getX(), missiles.get(e.getSource().getActiveDestMissile().getId()).getCoordinates().getY() + 60*e.getSource().getDestructLength()*missiles.get(e.getSource().getActiveDestMissile().getId()).getVelocity().getY());
       Point2D collisionPoint = new Point2D(ents.get(e.getSource().getActiveDestMissile().getId()).getCoordinates().getX() + 60*e.getSource().getDestructLength()*ents.get(e.getSource().getActiveDestMissile().getId()).getVelocity().getX(), ents.get(e.getSource().getActiveDestMissile().getId()).getCoordinates().getY() + 60*e.getSource().getDestructLength()*ents.get(e.getSource().getActiveDestMissile().getId()).getVelocity().getY());



        double angle = Math.atan2(collisionPoint.getY() - e.getSource().getCoordinates().getY(), collisionPoint.getX() - e.getSource().getCoordinates().getX()) * 180 / Math.PI +90;




       // AntiMissileInstance antiMissileI = new AntiMissileInstance(missileDestructors.get(e.getSource().getId()).getCoordinates(), collisionPoint, e.getSource().getDestructLength());
         AntiMissileInstance antiMissileI = new AntiMissileInstance(ents.get(e.getSource().getId()).getCoordinates(), collisionPoint, e.getSource().getDestructLength());

        antiMissileI.getView().setRotate(angle);

        addAntiMissile(e.getSource().getActiveDestMissile().getId(), antiMissileI);


    }

    @Override
    public synchronized void destroyAntiMissile(MissileDestructorEvent e) {
        //if(antiMissiles.containsKey(e.getSource().getActiveDestMissile().getId()))
          //  antiMissiles.get(e.getSource().getActiveDestMissile().getId()).setAlive(false);
        if(ents.containsKey(e.getSource().getActiveDestMissile().getId()))
            ents.get("AM" + e.getSource().getActiveDestMissile().getId()).setAlive(false);
    }
}


