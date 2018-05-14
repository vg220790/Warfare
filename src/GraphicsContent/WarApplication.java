package GraphicsContent;

import com.afekawar.bl.base.ConsoleVersion;
import com.afekawar.bl.base.Entities.Missile;
import com.afekawar.bl.base.Entities.MissileDestructor;
import com.afekawar.bl.base.Entities.MissileLauncher;
import com.afekawar.bl.base.Entities.MissileLauncherDestructor;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

import java.util.Map;

public class WarApplication extends Application {
    private Pane root;
    private Runnable mainProgram;

    private Map<String,GameObject> missiles = new HashMap<>();
    private Map<String,GameObject> missileLaunchers = new HashMap<>();
    private Map<String,GameObject> missileLauncherDestructors = new HashMap<>();
    private Map<String,GameObject> missileDestructors = new HashMap<>();

    private GameObject player;

    private Scene createContent(){
        root = new Pane();
        root.setPrefSize(1500,948);
        root.setId("pane");
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(this.getClass().getResource("resources/style.css").toExternalForm());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
                for(GameObject missile:missiles.values()){
                    if(missile.isAlive())
                        missile.update();
                    else missile.getView().setVisible(false);
                }
            }
        };
        timer.start();

        return scene;
    }

    private void onUpdate(){
        for(Runnable r : ((ConsoleVersion) mainProgram).entities.values()){

            if(r instanceof MissileLauncher){
                if(!((MissileLauncher) r).getHidden() && missileLaunchers.get(((MissileLauncher) r).getId()) == null) {
                    ImageView icon = new ImageView(new Image("GraphicsContent/resources/missileLauncher.png"));
                    icon.setScaleX(0.6);
                    icon.setScaleY(0.6);
                    MissileL launcher = new MissileL(((MissileLauncher) r).getCoordinates(), icon);
                    addMissileLauncher(((MissileLauncher) r).getId(),launcher, ((MissileLauncher) r).getCoordinates().getX() - icon.getImage().getWidth() / 2, ((MissileLauncher) r).getCoordinates().getY() - icon.getImage().getHeight() / 2);




                }

                if(!((MissileLauncher) r).getMissiles().isEmpty()) {
                    if (((MissileLauncher) r).isAlive() && ((MissileLauncher) r).getMissiles().peek().getState() == Missile.State.READY) {
                        Missile temp;
                        temp = ((MissileLauncher) r).getMissiles().peek();
                        temp.setState("INAIR");
                        ImageView missileIcon = new ImageView(new Image("GraphicsContent/resources/missile.png"));
                        missileIcon.setScaleX(0.4);
                        missileIcon.setScaleY(0.4);

                        double angle = Math.atan2(temp.getTarget().getCoordinates().getY() - ((MissileLauncher) r).getCoordinates().getY(), temp.getTarget().getCoordinates().getX() - ((MissileLauncher) r).getCoordinates().getX()) * 180 / Math.PI +90;


                        missileIcon.setRotate(angle);
                        MissileInstance missileI = new MissileInstance(((MissileLauncher) r).getCoordinates(), missileIcon, temp.getTarget().getCoordinates(), temp.getFlyTime());
                        addMissile(temp.getId(), missileI, ((MissileLauncher) r).getCoordinates().getX() - missileIcon.getImage().getWidth() / 2, ((MissileLauncher) r).getCoordinates().getY() - missileIcon.getImage().getHeight() / 2);
                        ((MissileLauncher) r).getMissiles().poll();
                    }
                }

                if(!((MissileLauncher) r).isAlive()){
                    //  root.getChildren().remove((missileLaunchers.get(((MissileLauncher) r).getId())));
                    // missileLaunchers.remove(((MissileLauncher) r).getId());

                    missileLaunchers.get(((MissileLauncher) r).getId()).getView().setVisible(false);

                }
                if(((MissileLauncher) r).getActiveMissileThread() != null){                                         // Check if missile died and remove it from views.
                    if(!((MissileLauncher) r).getActiveMissileThread().isAlive()){
                        missiles.get(((MissileLauncher) r).getActiveMissileThread().getName()).setAlive(false);
                    }
                }

                }
            else if(r instanceof MissileDestructor){
                ImageView icon = new ImageView(new Image("GraphicsContent/resources/missileDestructor.png"));
                icon.setScaleX(0.7);
                icon.setScaleY(0.7);
                MissileD destructor = new MissileD(((MissileDestructor) r).getCoordinates(),icon);
                addMissileDestructor(((MissileDestructor) r).getId(),destructor,((MissileDestructor) r).getCoordinates().getX() - icon.getImage().getWidth()/2 ,((MissileDestructor) r).getCoordinates().getY() - icon.getImage().getHeight()/2);
            }
            else if(r instanceof MissileLauncherDestructor){
                ImageView icon;
                if (((MissileLauncherDestructor) r).getType() == MissileLauncherDestructor.Type.BATTLESHIP)
                   icon = new ImageView(new Image("GraphicsContent/resources/battleship.png"));
                else
                    icon = new ImageView(new Image("GraphicsContent/resources/aircraft.png"));
                icon.setScaleX(0.6);
                icon.setScaleY(0.6);
                MissileLD launcherDestructor = new MissileLD(((MissileLauncherDestructor) r).getCoordinates(),icon);
                addMissileLauncherDestructor(((MissileLauncherDestructor) r).getId(), launcherDestructor,((MissileLauncherDestructor) r).getCoordinates().getX() - icon.getImage().getWidth()/2,((MissileLauncherDestructor) r).getCoordinates().getY() - icon.getImage().getHeight()/2);
            }



        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());




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



    private static class MissileL extends GameObject{
        public MissileL(Point2D coordinates, ImageView icon) {
            super(icon, coordinates);

        }
    }
    private static class MissileInstance extends GameObject{
        private Point2D velocity;
        public MissileInstance(Point2D coordinates, ImageView icon, Point2D targetCoordinates, int flyTime) {
            super(icon, coordinates);

            double distance = Math.sqrt((targetCoordinates.getY()-coordinates.getY())*(targetCoordinates.getY()-coordinates.getY()) + (targetCoordinates.getX()-coordinates.getX())*(targetCoordinates.getX()-coordinates.getX()) );

            double speed = distance / flyTime;

            speed/=60;

            double angle = Math.atan2(targetCoordinates.getY() - coordinates.getY(), targetCoordinates.getX() - coordinates.getX());

            velocity = new Point2D(Math.cos(angle) * speed, Math.sin(angle) * speed);
        }

        public void update(){
            this.getView().setTranslateX(this.getView().getTranslateX() + velocity.getX());
            this.getView().setTranslateY(this.getView().getTranslateY() + velocity.getY());
        }
    }
    private static class MissileLD extends GameObject{
        public MissileLD(Point2D coordinates,ImageView icon) {
            super(icon, coordinates);

        }
    }
    private static class MissileD extends GameObject{
        public MissileD(Point2D coordinates, ImageView icon) {
            super(icon, coordinates);

        }
    }


}


