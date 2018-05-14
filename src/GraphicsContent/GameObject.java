package GraphicsContent;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;



public class GameObject {
    private Node view;
    private Point2D coordinates;
    private Text name;
    private boolean isAlive = true;

    public void update(){
    }


    public boolean isAlive(){
        return isAlive;
    }

    public void setAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    public Node getView(){
        return view;
    }


    public GameObject(String id, ImageView view, Point2D coordinates){
        this.view = view;
        this.coordinates = coordinates;
        name = new Text(id);
        name.setX(coordinates.getX() + view.getImage().getWidth()/3);
        name.setY(coordinates.getY());
        name.setStyle("-fx-font: bold 18px \"Serif\"");
    }

    public Text getName(){
        return name;
    }
    public double getRotate() {
        return view.getRotate();
    }

    public void rotateRight(){
        view.setRotate(view.getRotate() + 5);
    }

    public void rotateLeft(){
        view.setRotate(view.getRotate() - 5);
    }

    public Point2D getCoordinates(){
        return coordinates;
    }
}
