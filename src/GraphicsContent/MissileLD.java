package GraphicsContent;

import com.afekawar.bl.base.Entities.MissileLauncherDestructor;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class MissileLD extends GameObject{
    private Point2D velocity;
    private float angle;
    private MissileLauncherDestructor.Type type;

    public MissileLD(String id, Point2D coordinates, ImageView icon, MissileLauncherDestructor.Type type) {
        super(id,icon, coordinates);
        this.type = type;
        angle = 0;
            velocity = new Point2D(0,0);

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