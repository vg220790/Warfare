package GraphicsContent;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class MissileInstance extends GameObject{
    private Point2D velocity;
    public MissileInstance(String id, Point2D coordinates, ImageView icon, Point2D targetCoordinates, int flyTime) {
        super(id,icon, coordinates);

        double distance = Math.sqrt((targetCoordinates.getY()-coordinates.getY())*(targetCoordinates.getY()-coordinates.getY()) + (targetCoordinates.getX()-coordinates.getX())*(targetCoordinates.getX()-coordinates.getX()) );

        double speed = distance / flyTime;

        speed/=60;

        double angle = Math.atan2(targetCoordinates.getY() - coordinates.getY(), targetCoordinates.getX() - coordinates.getX());

        velocity = new Point2D(Math.cos(angle) * speed, Math.sin(angle) * speed);
    }

    @Override
    public void update(){
        this.getView().setTranslateX(this.getView().getTranslateX() + velocity.getX());
        this.getView().setTranslateY(this.getView().getTranslateY() + velocity.getY());
        this.getName().setTranslateX(this.getName().getTranslateX() + velocity.getX());
        this.getName().setTranslateY(this.getName().getTranslateY() + velocity.getY());
    }
}