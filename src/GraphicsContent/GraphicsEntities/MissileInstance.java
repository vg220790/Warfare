package GraphicsContent.GraphicsEntities;

import GraphicsContent.GraphicsEntities.GameObject;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MissileInstance extends GameObject {
    static Image icon = new Image("GraphicsContent/Resources/missile.png");
    private Point2D velocity;
    private double speed;
    private double distance;
    public MissileInstance(String id, Point2D coordinates, Point2D targetCoordinates, int flyTime) {
        super(id, coordinates,icon);


        getView().setScaleX(0.4);
        getView().setScaleY(0.4);



        distance = Math.sqrt((targetCoordinates.getY()-coordinates.getY())*(targetCoordinates.getY()-coordinates.getY()) + (targetCoordinates.getX()-coordinates.getX())*(targetCoordinates.getX()-coordinates.getX()) );

        speed = distance / flyTime;

        speed/=60;

        double angle = Math.atan2(targetCoordinates.getY() - coordinates.getY(), targetCoordinates.getX() - coordinates.getX());

        velocity = new Point2D(Math.cos(angle) * speed, Math.sin(angle) * speed);
    }

    @Override
    public Point2D getVelocity(){
        return velocity;
    }
    public double getSpeed(){
        return speed;
    }
    public double getDistance(){
        return distance;
    }

    @Override
    public void update(){
        this.getView().setTranslateX(this.getView().getTranslateX() + velocity.getX());
        this.getView().setTranslateY(this.getView().getTranslateY() + velocity.getY());
        this.getName().setTranslateX(this.getName().getTranslateX() + velocity.getX());
        this.getName().setTranslateY(this.getName().getTranslateY() + velocity.getY());
    }
}