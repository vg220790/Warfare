package GraphicsContent;
import GraphicsContent.GraphicsEntities.*;
import JSONParser.MockEntities.BaseEntities.LD;
import JSONParser.MockEntities.BaseEntities.M;
import JSONParser.MockEntities.BaseEntities.MD;
import JSONParser.MockEntities.BaseEntities.ML;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;
import JSONParser.WarParser;
import SharedInterface.WarInterface;
import UI.ManualScenario;
import UI.Toast;
import com.afekawar.bl.base.Entities.MissileLauncherDestructor;
import com.afekawar.bl.base.Entities.Target;
import com.afekawar.bl.base.Interface.Communication.*;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.afekawar.bl.base.MainLogic;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    public GridPane createControlPanel(){
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(1);
        gridPane.setHgap(8);
        gridPane.setTranslateY(650);


        HBox launcherContainer = new HBox();
        Label addLauncher = new Label("Missile Launcher ID:  L");
        addLauncher.setTranslateY(5);
        launcherContainer.setPadding(new Insets(5));
        GridPane.setConstraints(launcherContainer,0,0);
        TextField launcherId = new TextField();
        launcherId.textProperty().addListener(new ChangeListener<String>() {                      // To restrict user input to only numbers with max 3-digit size
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    launcherId.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(launcherId.getText().length() > 3)
                    launcherId.setText(oldValue);
            }
        });
        launcherId.setMaxWidth(35);
        launcherId.setTranslateX(22);
        launcherContainer.getChildren().addAll(addLauncher,launcherId);

        CheckBox isHidden = new CheckBox("Is Hidden?");
        isHidden.setPadding(new Insets(10));
        GridPane.setConstraints(isHidden,1,0);
        Button addLauncherBtn = new Button("Add Missile Launcher");
        addLauncherBtn.setPadding(new Insets(10));
        addLauncherBtn.setPrefSize(155,40);
        addLauncherBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!launcherId.getText().equals("")) {
                    ML temp = new ML("L" + launcherId.getText(), isHidden.isSelected(), null);
                    boolean success = warInterface.addMissileLauncher(temp);
                    if (success) {
                        mainProgram.addWarEntity(temp);
                        toastMsg = "Missile Launcher " + temp.getId() + " was added...";
                        Toast.makeText(stage, toastMsg, ManualScenario.msgType.LOG);
                    } else {
                        toastMsg = "Failed to add Missile Launcher " + temp.getId();
                        Toast.makeText(stage, toastMsg, ManualScenario.msgType.ERROR);
                    }
                }
                else {
                    toastMsg = "Launcher ID can't be empty! ";
                    Toast.makeText(stage, toastMsg, ManualScenario.msgType.ERROR);
                }

            }
        });
        GridPane.setConstraints(addLauncherBtn,6,0);

        Label addLauncherDestructor = new Label("Missile Launcher Destructor: ");
        addLauncherDestructor.setPadding(new Insets(10));
        GridPane.setConstraints(addLauncherDestructor,0,1);
        Button addLauncherDestBtn = new Button("Add Launcher Destructor");

        addLauncherDestBtn.setPadding(new Insets(10));
        addLauncherDestBtn.setPrefSize(155,40);
        GridPane.setConstraints(addLauncherDestBtn,6,1);
        ComboBox<MissileLauncherDestructor.Type> types = new ComboBox<>();
        types.setItems(FXCollections.observableArrayList(MissileLauncherDestructor.Type.AIRCRAFT,MissileLauncherDestructor.Type.BATTLESHIP));
        types.setValue(MissileLauncherDestructor.Type.AIRCRAFT);
        types.setPadding(new Insets(10));
        types.setPrefWidth(150);
        GridPane.setConstraints(types,1,1);
        addLauncherDestBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String type;
                if(types.getValue().toString().equals("AIRCRAFT"))
                    type = "plane";
                else
                    type = "ship";
                LD temp = new LD(type,null);
                boolean success = warInterface.addMissileLauncherDestructor(temp);

                if(success) {
                    mainProgram.addWarEntity(temp);
                    toastMsg = "Missile Launcher Destructor " + temp.getId() + " was added...";
                    Toast.makeText(stage, toastMsg, ManualScenario.msgType.LOG);
                }
                else {
                    toastMsg = "Failed to add Missile Launcher Destructor " + temp.getId();
                    Toast.makeText(stage, toastMsg, ManualScenario.msgType.ERROR);
                }
            }
        });


        HBox launcherDestructorContainer = new HBox();
        Label addMissileDestructor = new Label("Missile Destructor ID:  MD");
        addMissileDestructor.setTranslateY(5);
        launcherDestructorContainer.setPadding(new Insets(5));
        GridPane.setConstraints(launcherDestructorContainer,0,2);
        TextField launcherDestId = new TextField();
        launcherDestId.textProperty().addListener(new ChangeListener<String>() {                      // To restrict user input to only numbers with max 3-digit size
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    launcherDestId.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(launcherDestId.getText().length() > 3)
                    launcherDestId.setText(oldValue);
            }
        });
        launcherDestId.setMaxWidth(35);
        launcherDestructorContainer.getChildren().addAll(addMissileDestructor,launcherDestId);

        Button addMissileDestBtn = new Button("Add Missile Destructor");
        addMissileDestBtn.setPadding(new Insets(10));
        addMissileDestBtn.setPrefSize(155,40);
        GridPane.setConstraints(addMissileDestBtn,6,2);
        addMissileDestBtn.setOnAction(event -> {
            MD temp = new MD("D" + launcherDestId.getText(),null);
            boolean success = warInterface.addMissileDestructor(temp);
            if(success) {
                mainProgram.addWarEntity(temp);
                toastMsg = "Missile Destructor " + temp.getId() + " was added...";
                Toast.makeText(stage, toastMsg, ManualScenario.msgType.LOG);
            } else {
                toastMsg = "Failed to add Missile Destructor " + temp.getId();
                Toast.makeText(stage, toastMsg, ManualScenario.msgType.ERROR);
            }
        });


        HBox missileContainer = new HBox();
        Label addMissile = new Label("Missile ID:  M");
        addMissile.setTranslateY(5);
        missileContainer.setPadding(new Insets(10));
        GridPane.setConstraints(missileContainer,0,3);
        TextField missileId = new TextField();
        missileId.textProperty().addListener(new ChangeListener<String>() {                      // To restrict user input to only numbers with max 3-digit size
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    missileId.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(missileId.getText().length() > 3)
                    missileId.setText(oldValue);
            }
        });
        missileId.setMaxWidth(35);
        missileId.setTranslateX(63);
        missileContainer.getChildren().addAll(addMissile,missileId);
        Button addMissileBtn = new Button("Add Missile");
        addMissileBtn.setPrefSize(155,40);
        addMissileBtn.setPadding(new Insets(10));
        GridPane.setConstraints(addMissileBtn,6,3);
        HBox targetContainer = new HBox();
        Label targetLabel = new Label("Target: ");
        targetLabel.setTranslateY(12);
        targetContainer.setPadding(new Insets(3));
        ComboBox<Target> targetsList = new ComboBox<>();
        targetContainer.getChildren().addAll(targetLabel,targetsList);
        GridPane.setConstraints(targetContainer,1,3);
        targetsList.setMaxHeight(200);
        targetsList.setVisibleRowCount(10);
        targetsList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getTargets()));
        targetsList.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                targetsList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getTargets()));
            }
        });
        HBox launchTimeContainer = new HBox();
        launchTimeContainer.setPadding(new Insets(10));
        Label launchTimeLabel = new Label("Launch Time: ");
        launchTimeLabel.setTranslateY(5);
        TextField launchTime = new TextField();
        launchTime.setTranslateX(37);
        launchTimeContainer.getChildren().addAll(launchTimeLabel,launchTime);
        GridPane.setConstraints(launchTimeContainer,2,3);
        launchTime.setMaxWidth(35);
        launchTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    launchTime.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(launchTime.getText().length() > 3)
                    launchTime.setText(oldValue);
            }
        });
        HBox flyTimeContainer = new HBox();
        Label flyTimeLabel = new Label("Fly Time: ");
        flyTimeLabel.setTranslateY(5);
        flyTimeContainer.setPadding(new Insets(10));
        TextField flyTime = new TextField("2");
        flyTime.setMaxWidth(35);
        GridPane.setConstraints(flyTimeContainer,3,3);
        flyTimeContainer.getChildren().addAll(flyTimeLabel,flyTime);
        flyTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    flyTime.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(flyTime.getText().length() > 2)
                    flyTime.setText(oldValue);
            }
        });
        HBox damageContainer = new HBox();
        damageContainer.setPadding(new Insets(10));
        Label damageLabel = new Label("Damage: ");
        damageLabel.setTranslateY(5);
        TextField damage = new TextField("1500");
        damageContainer.getChildren().addAll(damageLabel,damage);
        GridPane.setConstraints(damageContainer,4,3);
        damage.setMaxWidth(50);
        damage.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    damage.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(damage.getText().length() > 4)
                    damage.setText(oldValue);
            }
        });
        HBox sourceContainer = new HBox();
        GridPane.setConstraints(sourceContainer,5,3);
        sourceContainer.setPadding(new Insets(3));
        ComboBox<ML> launcherList = new ComboBox<>();
        launcherList.setTranslateX(4);
        launcherList.setVisibleRowCount(10);
        launcherList.setMaxHeight(200);
        launcherList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getMissileLaunchers()));
        launcherList.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                launcherList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getMissileLaunchers()));
            }
        });
        Label sourceLabel = new Label("Source Launcher: ");
        sourceLabel.setTranslateY(12);
        sourceContainer.getChildren().add(sourceLabel);
        sourceContainer.getChildren().add(launcherList);

        addMissileBtn.setOnAction(event -> {
            if (!missileId.getText().equals("") && targetsList.getValue() != null && !launchTime.getText().equals("") && !flyTime.getText().equals("") && !damage.getText().equals("") && launcherList.getValue() != null) {

                M temp = new M("M" + missileId.getText(), targetsList.getValue().toString(), launchTime.getText(), flyTime.getText(), damage.getText());
                boolean success = warInterface.addMissile(launcherList.getValue().toString(), temp);

                if (success) {
                    mainProgram.addMissileEntity(launcherList.getValue().toString(),temp);
                    toastMsg = "Missile " + temp.getId() + " was added to Launcher " + launcherList.getValue().toString();
                    Toast.makeText(stage, toastMsg, ManualScenario.msgType.LOG);
                } else {
                    toastMsg = "Missile " + temp.getId() + " failed to load!!! ";
                    Toast.makeText(stage, toastMsg, ManualScenario.msgType.ERROR);
                }

            }
            else {
                toastMsg = "Error in one of the Create Missile's fields";
                Toast.makeText(stage, toastMsg, ManualScenario.msgType.ERROR);
            }

        });


        Label addDestLauncherCommand = new Label("Add Dest Launcher Command:");
        addDestLauncherCommand.setPadding(new Insets(10));
        GridPane.setConstraints(addDestLauncherCommand,0,4);

        HBox launcherListContainer = new HBox();
        Label launcherListLabel = new Label("Target Launcher: ");
        ComboBox<ML> mlList = new ComboBox<>();
        mlList.setTranslateX(9);
        launcherListContainer.getChildren().addAll(launcherListLabel,mlList);
        GridPane.setConstraints(launcherListContainer,1,4);
        mlList.setVisibleRowCount(10);
        mlList.setMaxHeight(600);
        mlList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getMissileLaunchers()));
        mlList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mlList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getMissileLaunchers()));
            }
        });

        HBox timeValueContainer = new HBox();
        Label timeValueLabel = new Label("Destruct Time: ");
        timeValueLabel.setTranslateY(5);
        timeValueContainer.setPadding(new Insets(10));
        TextField timeValue = new TextField();
        timeValue.setTranslateX(30);
        timeValueContainer.getChildren().addAll(timeValueLabel,timeValue);
        GridPane.setConstraints(timeValueContainer,2,4);
        timeValue.setMaxWidth(35);
        timeValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    timeValue.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(timeValue.getText().length() > 3)
                    timeValue.setText(oldValue);
            }
        });
        HBox sourceDestContainer = new HBox();
        Label sourceDestLabel = new Label("Source Destructor: ");
        ComboBox<LD> mldList = new ComboBox<>();
        sourceDestContainer.getChildren().addAll(sourceDestLabel,mldList);
        GridPane.setConstraints(sourceDestContainer,5,4);
        mldList.setMaxHeight(500);
        mldList.setPrefWidth(100);
        mldList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getMissileLauncherDestructors()));
        mldList.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mldList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getMissileLauncherDestructors()));
            }
        });
        Button addCommand = new Button("Add Command");
        addCommand.setPrefSize(155,40);
        GridPane.setConstraints(addCommand,6,4);
        addCommand.setOnAction(event -> {
            if (mldList.getValue() != null && mlList.getValue() != null && !timeValue.getText().equals("")) {

                DestLauncher temp = new DestLauncher(mlList.getValue().getId(), timeValue.getText());
                warInterface.addDestLauncher(mldList.getValue().getId(), temp);
                mainProgram.addDestLauncherCommand(mldList.getValue().getId(),Integer.parseInt(timeValue.getText()),mlList.getValue().getId());
                toastMsg = "Destroy Launcher Command " + temp.getId() + " was added...";
                Toast.makeText(stage, toastMsg, ManualScenario.msgType.LOG);
            }
            else{
                toastMsg = "Error in one of the 'Destroy Launcher' fields";
                Toast.makeText(stage, toastMsg, ManualScenario.msgType.ERROR);
            }
        });

        Label addDestMissileCommand = new Label("Add Dest Missile Command:");
        addDestMissileCommand.setPadding(new Insets(10));
        GridPane.setConstraints(addDestMissileCommand,0,5);
        HBox missileListContainer = new HBox();
        Label missileListLabel = new Label("Target Missile: ");
        ComboBox<M> mList = new ComboBox<>();
        mList.setTranslateX(20);
        GridPane.setConstraints(missileListContainer,1,5);
        missileListContainer.getChildren().addAll(missileListLabel,mList);
        mList.setVisibleRowCount(10);
        mList.setMaxHeight(600);
        mList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getAllMissiles()));
        mList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getAllMissiles()));
            }
        });

        HBox missileTimeValueContainer = new HBox();
        missileTimeValueContainer.setTranslateY(7);
        Label missileTimeValueLabel = new Label("Destruct After Launch: ");
        timeValueLabel.setTranslateY(5);
        timeValueContainer.setPadding(new Insets(10));
        TextField missileTimeValue = new TextField();
        missileTimeValue.setMaxWidth(35);
        missileTimeValueContainer.getChildren().addAll(missileTimeValueLabel,missileTimeValue);
        GridPane.setConstraints(missileTimeValueContainer,2,5);
        timeValue.setMaxWidth(35);
        missileTimeValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    missileTimeValue.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(missileTimeValue.getText().length() > 3)
                    missileTimeValue.setText(oldValue);
            }
        });

        HBox sourceMissileDestContainer = new HBox();
        Label sourceMissileDestLabel = new Label("Source Destructor: ");
        ComboBox<MD> mdList = new ComboBox<>();
        sourceMissileDestContainer.getChildren().addAll(sourceMissileDestLabel,mdList);
        GridPane.setConstraints(sourceMissileDestContainer,5,5);
        mdList.setMaxHeight(500);
        mdList.setPrefWidth(100);
        mdList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getMissileDestructors()));
        mdList.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mdList.setItems(FXCollections.observableArrayList(((WarParser)warInterface).getMissileDestructors()));
            }
        });
        Button addMCommand = new Button("Add Command");
        addMCommand.setPrefSize(155,40);
        GridPane.setConstraints(addMCommand,6,5);
        addMCommand.setOnAction(event -> {
            if (mdList.getValue() != null && mList.getValue() != null && !missileTimeValue.getText().equals("")) {


                DestMissile temp = new DestMissile(mList.getValue().getId(), missileTimeValue.getText());
                warInterface.addDestMissile(mdList.getValue().getId(), temp);
                mainProgram.addDestMissileCommand(mdList.getValue().getId(),Integer.parseInt(missileTimeValue.getText()),mList.getValue().getId());

                toastMsg = "Destroy Missile Command " + temp.getId() + " was added...";
                Toast.makeText(stage, toastMsg, ManualScenario.msgType.LOG);
            } else {
                toastMsg = "Error in one of the 'Destroy Missile's fields";
                Toast.makeText(stage, toastMsg, ManualScenario.msgType.ERROR);
            }
        });






        gridPane.getChildren().addAll(addMCommand,addCommand,sourceMissileDestContainer,missileTimeValueContainer,missileListContainer,sourceDestContainer,timeValueContainer,launcherListContainer,sourceContainer,targetContainer,damageContainer,launchTimeContainer,flyTimeContainer,types,addMissileBtn,addMissileDestBtn,addLauncherDestBtn,addLauncherBtn,isHidden,launcherContainer,addLauncherDestructor,launcherDestructorContainer,missileContainer,addDestLauncherCommand,addDestMissileCommand);
        gridPane.setGridLinesVisible(true);

    return gridPane;


    }


}