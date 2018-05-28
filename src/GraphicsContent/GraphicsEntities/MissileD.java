package GraphicsContent.GraphicsEntities;


import javafx.geometry.Point2D;
import javafx.scene.image.Image;


public class MissileD extends GameObject {
    private static Image icon = new Image("GraphicsContent/Resources/missileDestructor.png");
    public MissileD(String id, Point2D coordinates) {
        super(id,coordinates.subtract(icon.getWidth()/2,icon.getHeight()/2),icon);
        getView().setScaleX(0.7);
        getView().setScaleY(0.7);

    }
}