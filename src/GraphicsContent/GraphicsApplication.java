package GraphicsContent;

// Max Golotsvan - 314148123
// Maksim Lyashenko - 317914877

import GraphicsContent.GraphicsEntities.*;
import SharedInterface.WarInterface;
import UI.Toast.msgType;
import UI.Toast;
import com.afekawar.bl.base.Entities.BaseEntities.*;
import com.afekawar.bl.base.Interface.Communication.*;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.afekawar.bl.base.MainLogic;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GraphicsApplication extends Application implements WarEventListener {
    private Pane root;
    private Stage stage;
    private WarInterface warInterface;
    private SystemTime time;
    private GridPane gridPane;
    private String toastMsg;
    private MainLogic mainProgram;


    public void setMainProgram(MainLogic mainProgram){
        this.mainProgram = mainProgram;
    }

    public GraphicsApplication(SystemTime time, WarInterface warInterface){
        this.warInterface = warInterface;
        this.time = time;
    }

    private Map<String,GameObject> graphicsEntities = new HashMap<>();


    private Scene createContent(){
        root = new Pane();
        root.setPrefSize(1500,948);
        root.setId("pane");
        Text timeView = new Text("");
        timeView.setStyle("-fx-font: bold 18px \"Serif\"");
        timeView.setX(10);
        timeView.setY(20);

        gridPane = createControlPanel();


        Button statisticsBtn = new Button("Show Statistics");
        statisticsBtn.setPrefSize(150,80);
        statisticsBtn.setTranslateX(root.getPrefWidth() - statisticsBtn.getPrefWidth());
        statisticsBtn.setOnAction(event -> {
                    toastMsg = warInterface.showStats();
                    Toast.makeText(stage, toastMsg, msgType.STATISTICS);
                });

        root.getChildren().add(statisticsBtn);
        root.getChildren().add(gridPane);

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(this.getClass().getResource("Resources/style.css").toExternalForm());
        root.getChildren().add(timeView);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public synchronized void handle(long now) {
                timeView.setText(String.valueOf(time.getTime()));


                Iterator<GameObject> it = graphicsEntities.values().iterator();

                while(it.hasNext()){
                    GameObject entity = it.next();
                    entity.update();

                    if(!entity.isAlive()){
                        it.remove();
                        break;
                    }

                    if(!root.getChildren().contains(entity.getView()))
                        addGameObject(entity);

                }

            }

        };
        timer.start();
        return scene;
    }
    @Override
    public void start(Stage primaryStage){
        stage = primaryStage;
        primaryStage.setScene(createContent());
        primaryStage.show();

        stage.setOnCloseRequest(event -> warInterface.haltSystem());

    }


    private void addGameObject(GameObject object){
            root.getChildren().add(object.getView());
            root.getChildren().add(object.getName());
    }

    @Override
    public synchronized void handleWarEvent(WarEvent e) {
        WarEvent.Event_Type type = e.getEventType();


        switch (type){

            case CREATE_LAUNCHER:
                MissileL launcher = new MissileL(e.getId(),e.getCoordinates());
                if(e.getHidden())
                    launcher.setHidden(true);
                graphicsEntities.put(e.getId(),launcher);
                break;

            case DESTROY_LAUNCHER:
                if(graphicsEntities.containsKey(e.getId()))
                    graphicsEntities.get(e.getId()).destroy();
                break;

            case LAUNCH_MISSILE:
               if(graphicsEntities.containsKey(e.getId()))
                    graphicsEntities.get(e.getId()).setHidden(false);
                MissileInstance missile = new MissileInstance(e.getMissileId(),e.getCoordinates(), e.getTargetCoordinates());

                graphicsEntities.put(e.getMissileId(), missile);
                break;

            case DESTROY_MISSILE:
                if(graphicsEntities.containsKey(e.getMissileId()))
                    graphicsEntities.get(e.getMissileId()).destroy();
                break;

            case HIDE_LAUNCHER:
                if(graphicsEntities.containsKey(e.getId()))
                    graphicsEntities.get(e.getId()).setHidden(true);
                break;

            case CREATE_MISSILE_LAUNCHER_DESTRUCTOR:
                MissileLD launcherDestructor = new MissileLD(e.getId(), e.getCoordinates(), e.getDestructorType());
                graphicsEntities.put(e.getId(), launcherDestructor);
                break;

            case LAUNCH_ANTI_MISSILE_LAUNCHER:
                AntiMissileLauncherInstance antiMissileLauncher = new AntiMissileLauncherInstance(e.getMissileId(),e.getCoordinates(), e.getTargetCoordinates());
                graphicsEntities.put(e.getMissileId(), antiMissileLauncher);
                break;

            case DESTROY_ANTI_MISSILE_LAUNCHER:
                if(graphicsEntities.containsKey(e.getMissileId()))
                    graphicsEntities.get(e.getMissileId()).destroy();
                break;

            case CREATE_MISSILE_DESTRUCTOR:
                MissileD destructor = new MissileD(e.getId(), e.getCoordinates());
                graphicsEntities.put(e.getId(), destructor);
                break;

            case LAUNCH_ANTI_MISSILE:
                AntiMissileInstance antiMissile = new AntiMissileInstance(e.getMissileId(),e.getCoordinates(), e.getTargetCoordinates());
                graphicsEntities.put(e.getMissileId(), antiMissile);
                break;

            case DESTROY_ANTI_MISSILE:
                if(graphicsEntities.containsKey(e.getMissileId()))
                    graphicsEntities.get(e.getMissileId()).destroy();
                break;

            case UPDATE_COORDINATES:
                if(graphicsEntities.containsKey(e.getId()))
                    graphicsEntities.get(e.getId()).setCoordinates(e.getCoordinates());
                break;
        }
    }

    private GridPane createControlPanel(){
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(1);
        gridPane.setHgap(8);
        gridPane.setTranslateY(600);


        HBox launcherContainer = new HBox();
        Label addLauncher = new Label("Missile Launcher ID:                 L");
        addLauncher.setTranslateY(5);
        launcherContainer.setPadding(new Insets(5));
        GridPane.setConstraints(launcherContainer,0,0);
        TextField launcherId = new TextField();
        // To restrict user input to only numbers with max 3-digit size
        launcherId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                launcherId.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(launcherId.getText().length() > 3)
                launcherId.setText(oldValue);
        });
        launcherId.setMaxWidth(45);

        launcherContainer.getChildren().addAll(addLauncher,launcherId);

        CheckBox isHidden = new CheckBox("Is Hidden?");
        isHidden.setPadding(new Insets(10));
        GridPane.setConstraints(isHidden,1,0);
        Button addLauncherBtn = new Button("Add Missile Launcher");
        addLauncherBtn.setPadding(new Insets(10));
        addLauncherBtn.setPrefSize(200,40);
        addLauncherBtn.setOnAction(event -> {
            if(!launcherId.getText().equals("")) {
                MissileLauncher temp = new MissileLauncher("L" + launcherId.getText(), isHidden.isSelected(), null);
                boolean success = warInterface.addMissileLauncher(temp);
                if (success) {
                    mainProgram.addWarEntity(temp);
                    toastMsg = "Missile Launcher " + temp.getId() + " was added...";
                    Toast.makeText(stage, toastMsg, msgType.LOG);
                } else {
                    toastMsg = "Failed to add Missile Launcher " + temp.getId();
                    Toast.makeText(stage, toastMsg, msgType.ERROR);
                }
            }
            else {
                toastMsg = "Launcher ID can't be empty! ";
                Toast.makeText(stage, toastMsg, msgType.ERROR);
            }

        });
        GridPane.setConstraints(addLauncherBtn,4,0);

        Label addLauncherDestructor = new Label("Missile Launcher Destructor: ");
        addLauncherDestructor.setPadding(new Insets(10));
        GridPane.setConstraints(addLauncherDestructor,0,1);
        Button addLauncherDestBtn = new Button("Add Launcher Destructor");

        addLauncherDestBtn.setPadding(new Insets(10));
        addLauncherDestBtn.setPrefSize(200,40);
        GridPane.setConstraints(addLauncherDestBtn,4,1);
        ComboBox<MissileLauncherDestructor.Type> types = new ComboBox<>();
        types.setItems(FXCollections.observableArrayList(MissileLauncherDestructor.Type.AIRCRAFT,MissileLauncherDestructor.Type.BATTLESHIP));
        types.setValue(MissileLauncherDestructor.Type.AIRCRAFT);
        types.setPadding(new Insets(10));
        types.setPrefWidth(150);
        GridPane.setConstraints(types,1,1);
        addLauncherDestBtn.setOnAction(event -> {
            String type;
            if(types.getValue().toString().equals("AIRCRAFT"))
                type = "plane";
            else
                type = "ship";
            MissileLauncherDestructor temp = new MissileLauncherDestructor(type,null);
            boolean success = warInterface.addMissileLauncherDestructor(temp);

            if(success) {
                mainProgram.addWarEntity(temp);
                toastMsg = "Missile Launcher Destructor " + temp.getId() + " was added...";
                Toast.makeText(stage, toastMsg, msgType.LOG);
            }
            else {
                toastMsg = "Failed to add Missile Launcher Destructor " + temp.getId();
                Toast.makeText(stage, toastMsg, msgType.ERROR);
            }
        });


        HBox launcherDestructorContainer = new HBox();
        Label addMissileDestructor = new Label("Missile Destructor ID:          MD");
        addMissileDestructor.setTranslateY(5);
        launcherDestructorContainer.setPadding(new Insets(5));
        GridPane.setConstraints(launcherDestructorContainer,0,2);
        TextField launcherDestId = new TextField();

        // To restrict user input to only numbers with max 3-digit size
        launcherDestId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                launcherDestId.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(launcherDestId.getText().length() > 3)
                launcherDestId.setText(oldValue);
        });
        launcherDestId.setMaxWidth(45);
        launcherDestructorContainer.getChildren().addAll(addMissileDestructor,launcherDestId);

        Button addMissileDestBtn = new Button("Add Missile Destructor");
        addMissileDestBtn.setPadding(new Insets(10));
        addMissileDestBtn.setPrefSize(200,40);
        GridPane.setConstraints(addMissileDestBtn,4,2);
        addMissileDestBtn.setOnAction(event -> {
            MissileDestructor temp = new MissileDestructor("D" + launcherDestId.getText(),null);
            boolean success = warInterface.addMissileDestructor(temp);
            if(success) {
                mainProgram.addWarEntity(temp);
                toastMsg = "Missile Destructor " + temp.getId() + " was added...";
                Toast.makeText(stage, toastMsg, msgType.LOG);
            } else {
                toastMsg = "Failed to add Missile Destructor " + temp.getId();
                Toast.makeText(stage, toastMsg, msgType.ERROR);
            }
        });


        HBox missileContainer = new HBox();
        Label addMissile = new Label("Missile ID:                              M");
        addMissile.setTranslateY(23);
        missileContainer.setPadding(new Insets(10));
        GridPane.setConstraints(missileContainer,0,3);
        TextField missileId = new TextField();
        missileId.setTranslateY(18);
        // To restrict user input to only numbers with max 3-digit size
        missileId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                missileId.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(missileId.getText().length() > 3)
                missileId.setText(oldValue);
        });
        missileId.setMaxWidth(45);

        missileContainer.getChildren().addAll(addMissile,missileId);
        Button addMissileBtn = new Button("Add Missile");
        addMissileBtn.setPrefSize(200,40);
        addMissileBtn.setPadding(new Insets(10));
        GridPane.setConstraints(addMissileBtn,4,3);
        HBox targetContainer = new HBox();
        Label targetLabel = new Label("Target: ");
        targetLabel.setTranslateY(30);
        targetContainer.setPadding(new Insets(3));
        ComboBox<Target> targetsList = new ComboBox<>();
        targetContainer.getChildren().addAll(targetLabel,targetsList);
        GridPane.setConstraints(targetContainer,1,3);
        targetsList.setMaxHeight(10);
        targetsList.setTranslateY(25);
        targetsList.setVisibleRowCount(10);
        targetsList.setItems(FXCollections.observableArrayList((warInterface).getTargets()));
        targetsList.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> targetsList.setItems(FXCollections.observableArrayList((warInterface).getTargets())));

        VBox missileValuesContainer = new VBox();

        HBox launchTimeContainer = new HBox();
        Label launchTimeLabel = new Label("Launch Time: ");
        launchTimeLabel.setTranslateY(5);
        TextField launchTimeText = new TextField("5");
        launchTimeContainer.getChildren().addAll(launchTimeLabel,launchTimeText);
        GridPane.setConstraints(launchTimeContainer,2,3);
        launchTimeText.setMaxWidth(45);
        launchTimeText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                launchTimeText.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(launchTimeText.getText().length() > 3)
                launchTimeText.setText(oldValue);
        });
        HBox flyTimeContainer = new HBox();
        Label flyTimeLabel = new Label("Fly Time: ");
        flyTimeLabel.setTranslateY(5);
        TextField flyTimeText = new TextField("12");
        flyTimeText.setMaxWidth(45);
        flyTimeText.setTranslateX(28);
        GridPane.setConstraints(flyTimeContainer,3,3);
        flyTimeContainer.getChildren().addAll(flyTimeLabel,flyTimeText);
        flyTimeText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                flyTimeText.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(flyTimeText.getText().length() > 2)
                flyTimeText.setText(oldValue);
        });
        HBox damageContainer = new HBox();
        Label damageLabel = new Label("Damage: ");
        damageLabel.setTranslateY(5);
        TextField damageText = new TextField("1500");
        damageText.setTranslateX(23);
        damageContainer.getChildren().addAll(damageLabel,damageText);
        GridPane.setConstraints(damageContainer,4,3);
        damageText.setMaxWidth(50);
        damageText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                damageText.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(damageText.getText().length() > 4)
                damageText.setText(oldValue);
        });
        HBox sourceContainer = new HBox();
        GridPane.setConstraints(sourceContainer,3,3);
        sourceContainer.setPadding(new Insets(3));
        ComboBox<MissileLauncher> launcherList = new ComboBox<>();
        launcherList.setTranslateX(4);
        launcherList.setVisibleRowCount(10);
        launcherList.setMaxHeight(10);
        launcherList.setTranslateY(25);
        launcherList.setItems(FXCollections.observableArrayList((warInterface).getMissileLaunchers()));
        launcherList.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> launcherList.setItems(FXCollections.observableArrayList((warInterface).getMissileLaunchers())));
        Label sourceLabel = new Label("Source Launcher: ");
        sourceLabel.setTranslateY(30);
        sourceContainer.getChildren().add(sourceLabel);
        sourceContainer.getChildren().add(launcherList);

        missileValuesContainer.getChildren().addAll(launchTimeContainer,flyTimeContainer,damageContainer);
        GridPane.setConstraints(missileValuesContainer,2,3);

        addMissileBtn.setOnAction(event -> {
            if (!missileId.getText().equals("") && targetsList.getValue() != null && !launchTimeText.getText().equals("") && !flyTimeText.getText().equals("") && !damageText.getText().equals("") && launcherList.getValue() != null) {

                int launchTime = Integer.parseInt(launchTimeText.getText());
                int flyTime = Integer.parseInt(flyTimeText.getText());
                int damage = Integer.parseInt(damageText.getText());
                Point2D coordinates =targetsList.getValue().getCoordinates();

                Missile temp = new Missile("M" + missileId.getText(), coordinates, launchTime, flyTime, damage,time);
                temp.setCoordinates(launcherList.getValue().getCoordinates());
                temp.setDestination(targetsList.getValue().toString());
                boolean success = warInterface.addMissile(launcherList.getValue().toString(), temp);

                if (success) {
                    toastMsg = "Missile " + temp.getId() + " was added to Launcher " + launcherList.getValue().toString();
                    Toast.makeText(stage, toastMsg, msgType.LOG);
                } else {
                    toastMsg = "Missile " + temp.getId() + " failed to load!!! ";
                    Toast.makeText(stage, toastMsg, msgType.ERROR);
                }

            }
            else {
                toastMsg = "Error in one of the Create Missile's fields";
                Toast.makeText(stage, toastMsg, msgType.ERROR);
            }

        });


        Label addDestLauncherCommand = new Label("Add Dest Launcher Command:");
        addDestLauncherCommand.setPadding(new Insets(10));
        GridPane.setConstraints(addDestLauncherCommand,0,4);

        HBox launcherListContainer = new HBox();
        Label launcherListLabel = new Label("Target Launcher: ");
        ComboBox<MissileLauncher> mlList = new ComboBox<>();
        mlList.setTranslateX(9);
        launcherListContainer.getChildren().addAll(launcherListLabel,mlList);
        GridPane.setConstraints(launcherListContainer,1,4);
        mlList.setVisibleRowCount(10);
        mlList.setMaxHeight(600);
        mlList.setItems(FXCollections.observableArrayList((warInterface).getMissileLaunchers()));
        mlList.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mlList.setItems(FXCollections.observableArrayList((warInterface).getMissileLaunchers())));


        HBox sourceDestContainer = new HBox();
        Label sourceDestLabel = new Label("Source Destructor: ");
        ComboBox<MissileLauncherDestructor> mldList = new ComboBox<>();
        sourceDestContainer.getChildren().addAll(sourceDestLabel,mldList);
        GridPane.setConstraints(sourceDestContainer,3,4);
        mldList.setMaxHeight(500);
        mldList.setPrefWidth(100);
        mldList.setItems(FXCollections.observableArrayList((warInterface).getMissileLauncherDestructors()));
        mldList.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> mldList.setItems(FXCollections.observableArrayList((warInterface).getMissileLauncherDestructors())));
        Button addCommand = new Button("Add Command");
        addCommand.setPrefSize(200,40);
        GridPane.setConstraints(addCommand,4,4);
        addCommand.setOnAction(event -> {
            if (mldList.getValue() != null && mlList.getValue() != null) {

                warInterface.addDestLauncher(mldList.getValue().getId(),mlList.getValue().getId(),time.getTime() + 5);
                toastMsg = "Destroy Launcher Command Added";
                Toast.makeText(stage, toastMsg, msgType.LOG);
            }
            else{
                toastMsg = "Error in one of the 'Destroy Launcher' fields";
                Toast.makeText(stage, toastMsg, msgType.ERROR);
            }
        });

        Label addDestMissileCommand = new Label("Add Dest Missile Command:");
        addDestMissileCommand.setPadding(new Insets(10));
        GridPane.setConstraints(addDestMissileCommand,0,5);
        HBox missileListContainer = new HBox();
        Label missileListLabel = new Label("Target Missile: ");
        ComboBox<Missile> mList = new ComboBox<>();
        mList.setTranslateX(20);
        GridPane.setConstraints(missileListContainer,1,5);
        missileListContainer.getChildren().addAll(missileListLabel,mList);
        mList.setVisibleRowCount(10);
        mList.setMaxHeight(600);
        mList.setItems(FXCollections.observableArrayList((warInterface).getAllMissiles()));
        mList.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mList.setItems(FXCollections.observableArrayList((warInterface).getAllMissiles())));


        HBox sourceMissileDestContainer = new HBox();
        Label sourceMissileDestLabel = new Label("Source Destructor: ");
        ComboBox<MissileDestructor> mdList = new ComboBox<>();
        sourceMissileDestContainer.getChildren().addAll(sourceMissileDestLabel,mdList);
        GridPane.setConstraints(sourceMissileDestContainer,3,5);
        mdList.setMaxHeight(500);
        mdList.setPrefWidth(100);
        mdList.setItems(FXCollections.observableArrayList((warInterface).getMissileDestructors()));
        mdList.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> mdList.setItems(FXCollections.observableArrayList((warInterface).getMissileDestructors())));
        Button addMCommand = new Button("Add Command");
        addMCommand.setPrefSize(200,40);
        GridPane.setConstraints(addMCommand,4,5);
        addMCommand.setOnAction(event -> {
            if (mdList.getValue() != null && mList.getValue() != null) {
                mdList.getValue().setMissileToDestroy(mList.getValue());
                toastMsg = "Destroy Missile Command Added";
                Toast.makeText(stage, toastMsg, msgType.LOG);
            } else {
                toastMsg = "Error in one of the 'Destroy Missile's fields";
                Toast.makeText(stage, toastMsg, msgType.ERROR);
            }
        });






        gridPane.getChildren().addAll(addMCommand,addCommand,sourceMissileDestContainer,missileListContainer,sourceDestContainer,launcherListContainer,sourceContainer,targetContainer,missileValuesContainer,types,addMissileBtn,addMissileDestBtn,addLauncherDestBtn,addLauncherBtn,isHidden,launcherContainer,addLauncherDestructor,launcherDestructorContainer,missileContainer,addDestLauncherCommand,addDestMissileCommand);
        gridPane.setGridLinesVisible(true);

    return gridPane;


    }


}