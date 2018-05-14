package GraphicsContent;

import javafx.geometry.Point2D;
import javafx.scene.Node;



public class GameObject {
    private Node view;
    private Point2D coordinates;

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


    public GameObject(Node view, Point2D coordinates){
        this.view = view;
        this.coordinates = coordinates;
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
