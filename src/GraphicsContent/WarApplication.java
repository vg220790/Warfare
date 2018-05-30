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

import java.util.Iterator;
import java.util.Map;

public class WarApplication extends Application implements WarEventListener {
    private Pane root;
    private SystemTime time;

    private Map<String,GameObject> graphicsEntities = new HashMap<>();

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

                Iterator<GameObject> it = graphicsEntities.values().iterator();

                while(it.hasNext()){
                    GameObject entity = it.next();
                    entity.update();

                    if(!entity.isAlive()){
                        it.remove();
                        break;
                    }
                    if(!root.getChildren().contains(entity.getView()))
                        addGameObject(entity,entity.getCoordinates().getX(),entity.getCoordinates().getY());
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


    private void addGameObject(GameObject object, double x, double y){
            object.getView().setX(x);
            object.getView().setY(y);
            root.getChildren().add(object.getView());
            root.getChildren().add(object.getName());
    }

    /*

    @Override
    public synchronized void createMissileLauncher(WarEvent e) {
        MissileL launcher = new MissileL(e.getSource().getId(),e.getSource().getCoordinates());
        if(e.getSource().getHidden())
            launcher.setHidden(true);
        graphicsEntities.put(e.getSource().getId(),launcher);
        }

    @Override
    public synchronized void destroyMissileLauncher(WarEvent e) {
        if(graphicsEntities.containsKey(e.getSource().getId())) {
           graphicsEntities.get(e.getSource().getId()).destroy();

        }
    }

    @Override
    public synchronized void launchMissile(WarEvent e) {
        double angle = Math.atan2(e.getSource().getActiveMissileEntity().getTarget().getCoordinates().getY() - graphicsEntities.get(e.getSource().getId()).getCoordinates().getY(), e.getSource().getActiveMissileEntity().getTarget().getCoordinates().getX() - graphicsEntities.get(e.getSource().getId()).getCoordinates().getX()) * 180 / Math.PI +90;
        if(graphicsEntities.containsKey(e.getSource().getId()))
            graphicsEntities.get(e.getSource().getId()).setHidden(false);

        MissileInstance missile = new MissileInstance(e.getSource().getActiveMissileEntity().getId(),e.getSource().getCoordinates(), e.getSource().getActiveMissileEntity().getTarget().getCoordinates(),e.getSource().getActiveMissileEntity().getFlyTime());
        missile.getView().setRotate(angle);

        graphicsEntities.put(e.getSource().getActiveMissileEntity().getId(), missile);
    }

    @Override
    public synchronized void destroyMissile(WarEvent e){
        if(graphicsEntities.containsKey(e.getDestroyedMissileId()))
            graphicsEntities.get(e.getDestroyedMissileId()).destroy();
    }

    @Override
    public synchronized void hideMissileLauncher(WarEvent e){
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
    */

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
                angle = Math.atan2(e.getTargetCoordinates().getY() - graphicsEntities.get(e.getId()).getCoordinates().getY(), e.getTargetCoordinates().getX() - graphicsEntities.get(e.getId()).getCoordinates().getX()) * 180 / Math.PI +90;
                if(graphicsEntities.containsKey(e.getId()))
                    graphicsEntities.get(e.getId()).setHidden(false);

                MissileInstance missile = new MissileInstance(e.getMissileId(),e.getCoordinates(), e.getTargetCoordinates(),e.getFlyTime());
                missile.getView().setRotate(angle);

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
                collisionPoint = new Point2D(e.getTargetCoordinates().getX(), e.getTargetCoordinates().getY());
                angle = Math.atan2(collisionPoint.getY() - graphicsEntities.get(e.getId()).getCoordinates().getY(), collisionPoint.getX() - graphicsEntities.get(e.getId()).getCoordinates().getX()) * 180 / Math.PI +90;
                AntiMissileLauncherInstance antiMissileLauncherI = new AntiMissileLauncherInstance(e.getCoordinates().add(new Point2D(graphicsEntities.get(e.getId()).getView().getTranslateX(), graphicsEntities.get(e.getId()).getView().getTranslateY())), collisionPoint, e.getDestructLength());
                antiMissileLauncherI.getView().setRotate(angle);
                graphicsEntities.put("AML" + e.getId(), antiMissileLauncherI);
                break;

            case DESTROY_ANTI_MISSILE_LAUNCHER:
                if(graphicsEntities.containsKey("AML" + e.getId()))
                    graphicsEntities.get("AML"+e.getId()).destroy();
                break;

            case CREATE_MISSILE_DESTRUCTOR:
                MissileD destructor = new MissileD(e.getId(), e.getCoordinates());
                graphicsEntities.put(e.getId(), destructor);
                break;

            case LAUNCH_ANTI_MISSILE:
                collisionPoint = new Point2D(graphicsEntities.get(e.getMissileId()).getCoordinates().getX()+ graphicsEntities.get(e.getMissileId()).getView().getImage().getWidth()/2 + 60*e.getDestructLength()* graphicsEntities.get(e.getMissileId()).getVelocity().getX(), graphicsEntities.get(e.getMissileId()).getCoordinates().getY()+ graphicsEntities.get(e.getMissileId()).getView().getImage().getHeight()/2 + 60*e.getDestructLength()* graphicsEntities.get(e.getMissileId()).getVelocity().getY());
                angle = Math.atan2(collisionPoint.getY() - e.getCoordinates().getY(), collisionPoint.getX() - e.getCoordinates().getX()) * 180 / Math.PI +90;
                AntiMissileInstance antiMissileI = new AntiMissileInstance(e.getCoordinates(), collisionPoint, e.getDestructLength());
                antiMissileI.getView().setRotate(angle);
                graphicsEntities.put("AM" + e.getId(), antiMissileI);
                break;

            case DESTROY_ANTI_MISSILE:
                if(graphicsEntities.containsKey("AM" + e.getId()))
                    graphicsEntities.get("AM" + e.getId()).destroy();
                break;

        }

    }

}