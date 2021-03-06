package UI;

// Max Golotsvan - 314148123
// Maksim Lyashenko - 317914877

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public final class Toast
{
    public enum msgType {LOG,ERROR,STATISTICS}
    public static void makeText(Stage ownerStage, String toastMsg, msgType messageType)
    {
        int toastDelay;
        int fadeInDelay;
        int fadeOutDelay;

        if(messageType == msgType.STATISTICS){
            fadeOutDelay = 3000;
            toastDelay = 3000;
            fadeInDelay = 500;
        }
        else{
            fadeOutDelay = 500;
            toastDelay = 1500;
            fadeInDelay = 500;
        }


        Stage toastStage=new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(toastMsg);
        text.setFont(Font.font("Verdana", 20));
        if(messageType == msgType.ERROR)
            text.setFill(Color.RED);
        else if(messageType == msgType.LOG)
            text.setFill(Color.BLUE);
        else
            text.setFill(Color.YELLOW);


        VBox root = new VBox();
        root.getChildren().add(text);
        root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.4); -fx-padding: 20px;");
        if(messageType == msgType.STATISTICS){
            root.setTranslateY(20);
        }
        else
            root.setTranslateY(900);
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.show();

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        fadeInTimeline.setOnFinished((ae) ->
                new Thread(() -> {
                    try
                    {
                        Thread.sleep(toastDelay);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 0));
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                    fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                    fadeOutTimeline.play();
                }).start());
        fadeInTimeline.play();
    }

}