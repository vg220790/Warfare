package GraphicsContent;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args){
        System.setProperty("quantum.multithreaded", "false");


        Application.launch(WarApplication.class,args);
    }

}
