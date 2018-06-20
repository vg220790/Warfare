package GraphicsContent.GraphicsEntities;

import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncherDestructor;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class MissileLD extends GameObject {
    private static Image icon = new Image("GraphicsContent/Resources/battleship.png");
    private float angle;
    private MissileLauncherDestructor.Type type;

    public MissileLD(String id, Point2D coordinates, MissileLauncherDestructor.Type type) {
        super(id, coordinates.subtract(icon.getWidth()/2,icon.getHeight()/2),icon);

        this.type = type;
        angle = 0;
        if (type == MissileLauncherDestructor.Type.BATTLESHIP)
            getView().setImage(new Image("GraphicsContent/Resources/battleship.png"));
        else
            getView().setImage(new Image("GraphicsContent/Resources/aircraft.png"));
        getView().setScaleX(0.6);
        getView().setScaleY(0.6);

    }

    @Override
    public void update(){
        if(this.type == MissileLauncherDestructor.Type.AIRCRAFT){
            angle += Math.PI/16;
            this.getView().setRotate(angle);

        }

        this.getView().setX(this.getCoordinates().getX() - icon.getWidth()/2);
        this.getView().setY(this.getCoordinates().getY()- icon.getHeight()/2);
        this.getName().setX(this.getCoordinates().getX() - 10);
        this.getName().setY(this.getCoordinates().getY() - 20);
    }
}