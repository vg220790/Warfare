package GraphicsContent.GraphicsEntities;

import javafx.geometry.Point2D;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;



public abstract class GameObject {
    private ImageView view;
    private Point2D coordinates;
    private Text nameView;
    private boolean isAlive;
    private boolean isHidden;

    public void update(){
        if(!isAlive) {
            view.setVisible(false);
            nameView.setVisible(false);
        }
    }

    public boolean isAlive(){
        return isAlive;
    }

    void setAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    public ImageView getView(){
        return view;
    }

    public void destroy(){
    }


    GameObject(String id, Point2D coordinates, Image icon){
        setAlive(true);
        setHidden(false);
        this.view = new ImageView(icon);
        this.coordinates = coordinates;
        nameView = new Text(id);
        nameView.setX(coordinates.getX() + view.getImage().getWidth()/3);
        nameView.setY(coordinates.getY());
        nameView.setStyle("-fx-font: bold 18px \"Serif\"");
        this.getView().setX(coordinates.getX());
        this.getView().setY(coordinates.getY());

    }
    boolean isHidden(){
        return isHidden;
    }
    public void setHidden(boolean isHidden){
        this.isHidden = isHidden;
    }
    public Text getName(){
        return nameView;
    }
    public Point2D getCoordinates(){
        return new Point2D(coordinates.getX(),coordinates.getY());
    }
    public void setCoordinates(Point2D coordinates){
        this.coordinates = coordinates;
    }

}
