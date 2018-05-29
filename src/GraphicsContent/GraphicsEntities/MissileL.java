package GraphicsContent.GraphicsEntities;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;


public class MissileL extends GameObject {
    private static Image icon = new Image("GraphicsContent/Resources/missileLauncher.png");
    public MissileL(String id, Point2D coordinates) {
        super(id, coordinates.subtract(icon.getWidth()/2,icon.getHeight()/2),icon);
        getView().setScaleX(0.6);
        getView().setScaleY(0.6);

    }

    @Override
    public void destroy(){
        setAlive(false);

    }

    @Override
    public void update(){
        if(!isAlive())
            this.getView().setImage((new Image("GraphicsContent/Resources/destroyedMissileLauncher.png")));

        if(isHidden())
            getView().setOpacity(0.25);
        else
            getView().setOpacity(1);
    }


}