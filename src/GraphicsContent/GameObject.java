package GraphicsContent;

import javafx.geometry.Point2D;

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

    public void destroy(){

    }


    public GameObject(String id, ImageView view, Point2D coordinates){
        this.view = view;
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

}
