package GraphicsContent.GraphicsEntities;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;


public class MissileL extends GameObject {
    static Image icon = new Image("GraphicsContent/Resources/missileLauncher.png");
    public MissileL(String id, Point2D coordinates) {
        super(id, coordinates,icon);
        getView().setScaleX(0.6);
        getView().setScaleY(0.6);

    }

    @Override
    public void destroy(){
        this.getView().setImage((new Image("GraphicsContent/Resources/destroyedMissileLauncher.png")));
    }

}