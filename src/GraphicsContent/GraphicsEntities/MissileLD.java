package GraphicsContent.GraphicsEntities;

import GraphicsContent.GraphicsEntities.GameObject;
import com.afekawar.bl.base.Entities.MissileLauncherDestructor;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MissileLD extends GameObject {
    static Image icon = new Image("GraphicsContent/Resources/battleship.png");
    private Point2D velocity;
    private float angle;
    private MissileLauncherDestructor.Type type;

    public MissileLD(String id, Point2D coordinates, MissileLauncherDestructor.Type type) {
        super(id, coordinates,icon);

        this.type = type;
        angle = 0;
        velocity = new Point2D(0,0);
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
            velocity = new Point2D(Math.cos(angle/60),  Math.sin(angle/60));
        }
        this.getView().setTranslateX(this.getView().getTranslateX() + velocity.getX());
        this.getView().setTranslateY(this.getView().getTranslateY() + velocity.getY());
        this.getName().setTranslateX(this.getName().getTranslateX() + velocity.getX());
        this.getName().setTranslateY(this.getName().getTranslateY() + velocity.getY());
    }
}