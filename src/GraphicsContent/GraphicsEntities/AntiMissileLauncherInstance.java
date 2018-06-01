package GraphicsContent.GraphicsEntities;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class AntiMissileLauncherInstance extends GameObject{
    private static Image icon = new Image("GraphicsContent/Resources/missile.png");
    public AntiMissileLauncherInstance(String id, Point2D coordinates, Point2D targetCoordinates) {
        super(id, coordinates,icon);


        getView().setScaleX(0.3);
        getView().setScaleY(0.3);

        double angle = Math.atan2(targetCoordinates.getY() - coordinates.getY(), targetCoordinates.getX() - coordinates.getX()) * 180 / Math.PI +90;
        getView().setRotate(angle);


    }


    @Override
    public void destroy(){
        this.setAlive(false);
    }

    @Override
    public void update(){

        if(this.getCoordinates().getX() < -100 || this.getCoordinates().getX() > 1600)
            this.destroy();
        if(this.getCoordinates().getY() < -100 || this.getCoordinates().getY() > 1100)
            this.destroy();

        super.update();

        this.getView().setX(this.getCoordinates().getX() - icon.getWidth()/2);
        this.getView().setY(this.getCoordinates().getY()- icon.getHeight()/2);
        this.getName().setX(this.getCoordinates().getX() - 10);
        this.getName().setY(this.getCoordinates().getY() - 20);



    }

}

