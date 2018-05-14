package GraphicsContent.GraphicsEntities;

import javafx.geometry.Point2D;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;



public class GameObject {
    private ImageView view;
    private Point2D coordinates;
    private Text nameView;
    private boolean isAlive = true;
    private boolean isHidden = false;

    public void update(){
    }


    public boolean isAlive(){
        return isAlive;
    }

    public void setAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    public ImageView getView(){
        return view;
    }

    public void destroy(){ }
    public Point2D getVelocity(){
        return null;
    }


    GameObject(String id, Point2D coordinates, Image icon){
        this.view = new ImageView(icon);
        this.coordinates = coordinates;
        nameView = new Text(id);
        nameView.setX(coordinates.getX() + view.getImage().getWidth()/3);
        nameView.setY(coordinates.getY());
        nameView.setStyle("-fx-font: bold 18px \"Serif\"");

    }
    public boolean isHidden(){
        return isHidden;
    }
    public void setHidden(boolean isHidden){
        this.isHidden = isHidden;
    }
    public Text getName(){
        return nameView;
    }
    public Point2D getCoordinates(){
        return new Point2D(coordinates.getX() + getView().getTranslateX(),coordinates.getY() + getView().getTranslateY());
    }

}
