package GraphicsContent;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MissileL extends GameObject{

    public MissileL(String id, Point2D coordinates, ImageView icon) {
        super(id,icon, coordinates);

    }

    @Override
    public void destroy(){
        this.getView().setImage((new Image("GraphicsContent/resources/destroyedMissileLauncher.png")));
    }

}