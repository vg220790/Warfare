package UI;

import JSONParser.MockEntities.BaseEntities.M;
import JSONParser.MockEntities.BaseEntities.MD;
import JSONParser.MockEntities.BaseEntities.ML;
import JSONParser.MockEntities.BaseEntities.LD;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;
import JSONParser.WarParser;
import com.afekawar.bl.base.Entities.MissileLauncherDestructor;
import com.afekawar.bl.base.Entities.Target;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManualScenario extends Scene {
    private HBox addLauncher;
    private HBox addLauncherDestructor;
    private HBox destroyLauncher;
    private HBox addMissileDestructor;
    private HBox destroyMissile;
    private HBox addMissile;
    private Insets hboxPositions;
    public enum msgType {LOG,ERROR}
    SystemTime time;
    private String toastMsg;
    private Scene scene2;
    private Scene scene1;
    private Stage window;
    ManualScenario(VBox root, double width, double height, WarParser parsedEntities, Stage window, Scene prevScene, SystemTime time) {
        super(root, width, height);



        scene1 = root.getScene();
        hboxPositions = new Insets(root.getHeight()/12,0,0,0);

        addLauncher = new HBox();
        addLauncher.getChildren().add(new Text("Add Missile Launcher: ID: "));
        addLauncher.setPadding(hboxPositions);
        Label l = new Label("L");
        l.setFont(Font.font("Verdana", 20));
        l.setTranslateX(10);
        addLauncher.getChildren().add(l);
        TextField idInput = new TextField();

        idInput.setMaxWidth(35);
        idInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    idInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(idInput.getText().length() > 3)
                    idInput.setText(oldValue);
            }
        });

        idInput.setTranslateX(10);
        CheckBox isHiddenInput = new CheckBox("isHidden?");
        isHiddenInput.setTranslateX(20);
        Button addMissileLauncher = new Button("Add Missile Launcher");
        addMissileLauncher.setTranslateX(30);
        addMissileLauncher.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!idInput.getText().equals("")) {
                    ML temp = new ML("L" + idInput.getText(), isHiddenInput.isSelected(), null);
                    boolean success = parsedEntities.addMissileLauncher(temp);
                    if (success) {
                        toastMsg = "Missile Launcher " + temp.getId() + " was added...";
                        Toast.makeText(window, toastMsg, msgType.LOG);
                    } else {
                        toastMsg = "Failed to add Missile Launcher " + temp.getId();
                        Toast.makeText(window, toastMsg, msgType.ERROR);
                    }
                }
                else {
                    toastMsg = "Launcher ID can't be empty! ";
                    Toast.makeText(window, toastMsg, msgType.ERROR);
                }

            }
        });
        addLauncher.getChildren().add(idInput);
        addLauncher.getChildren().add(isHiddenInput);
        addLauncher.getChildren().add(addMissileLauncher);

        addLauncherDestructor = new HBox();
        addLauncherDestructor.getChildren().add(new Text("Add Missile Launcher Destructor: "));
        addLauncherDestructor.setPadding(hboxPositions);
        ComboBox<MissileLauncherDestructor.Type> types = new ComboBox<>();
        types.setTranslateX(10);
        types.setItems(FXCollections.observableArrayList(MissileLauncherDestructor.Type.AIRCRAFT,MissileLauncherDestructor.Type.BATTLESHIP));
        types.setValue(MissileLauncherDestructor.Type.AIRCRAFT);
        addLauncherDestructor.getChildren().add(types);
        Button addMissileLauncherDestructorBtn = new Button("Add Missile Launcher Destructor");
        addMissileLauncherDestructorBtn.setTranslateX(20);
        addMissileLauncherDestructorBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String type;
                if(types.getValue().toString().equals("AIRCRAFT"))
                    type = "plane";
                else
                    type = "ship";
                LD temp = new LD(type,null);
                boolean success = parsedEntities.addMissileLauncherDestructor(temp);

                if(success) {
                    toastMsg = "Missile Launcher Destructor " + temp.getId() + " was added...";
                    Toast.makeText(window, toastMsg, msgType.LOG);
                }
                else {
                    toastMsg = "Failed to add Missile Launcher Destructor " + temp.getId();
                    Toast.makeText(window, toastMsg, msgType.ERROR);
                }
            }
        });
        addLauncherDestructor.getChildren().add(addMissileLauncherDestructorBtn);

        destroyLauncher = new HBox();
        destroyLauncher.setSpacing(10);
        destroyLauncher.setPadding(new Insets(30,0,0,root.getWidth()/3));
        destroyLauncher.getChildren().add(new Text("Attemt to Destroy Launcher "));
        ComboBox<ML> mlList = new ComboBox<>();
        mlList.setVisibleRowCount(10);
        mlList.setMaxHeight(600);
        mlList.setItems(FXCollections.observableArrayList(parsedEntities.getMissileLaunchers()));
        mlList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mlList.setItems(FXCollections.observableArrayList(parsedEntities.getMissileLaunchers()));
            }
        });
        destroyLauncher.getChildren().add(mlList);
        destroyLauncher.getChildren().add(new Text("At "));
        TextField timeValue = new TextField();
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
        destroyLauncher.getChildren().add(timeValue);
        destroyLauncher.getChildren().add(new Text("seconds."));
        destroyLauncher.getChildren().add(new Text("Source Destructor: "));
        ComboBox<LD> mldList = new ComboBox<>();
       // mldList.setVisibleRowCount(10);
        mldList.setMaxHeight(500);
        mldList.setPrefWidth(100);
        mldList.setItems(FXCollections.observableArrayList(parsedEntities.getMissileLauncherDestructors()));
        mldList.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mldList.setItems(FXCollections.observableArrayList(parsedEntities.getMissileLauncherDestructors()));
            }
        });
        destroyLauncher.getChildren().add(mldList);
        Button addCommand = new Button("Add Command");
        addCommand.setOnAction(event -> {
            if (mldList.getValue() != null && mlList.getValue() != null && !timeValue.getText().equals("")) {

                DestLauncher temp = new DestLauncher(mlList.getValue().getId(), timeValue.getText());
                parsedEntities.addDestLauncher(mldList.getValue().getId(), temp);

                toastMsg = "Destroy Launcher Command " + temp.getId() + " was added...";
                Toast.makeText(window, toastMsg, msgType.LOG);
            }
            else{
                toastMsg = "Error in one of the 'Destroy Launcher' fields";
                Toast.makeText(window, toastMsg, msgType.ERROR);
            }
        });

        destroyLauncher.getChildren().add(addCommand);



        addMissileDestructor = new HBox();
        addMissileDestructor.getChildren().add(new Text("Add Missile Destructor: ID: "));
        addMissileDestructor.setPadding(hboxPositions);

        TextField idMDInput = new TextField();

        idMDInput.setMaxWidth(35);
        idMDInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    idMDInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(idMDInput.getText().length() > 3)
                    idMDInput.setText(oldValue);
            }
        });

        idMDInput.setTranslateX(10);
        Label d = new Label("D");
        d.setTranslateX(10);
        d.setFont(Font.font("Verdana", 20));
        addMissileDestructor.getChildren().add(d);

        Button addMissileDestructorBtn = new Button("Add Missile Destructor");
        addMissileDestructorBtn.setTranslateX(20);
        addMissileDestructorBtn.setOnAction(event -> {
            MD temp = new MD("D" + idMDInput.getText(),null);
            boolean success = parsedEntities.addMissileDestructor(temp);
            if(success) {
                toastMsg = "Missile Destructor " + temp.getId() + " was added...";
                Toast.makeText(window, toastMsg, msgType.LOG);
            } else {
                toastMsg = "Failed to add Missile Destructor " + temp.getId();
                Toast.makeText(window, toastMsg, msgType.ERROR);
            }
        });
        addMissileDestructor.getChildren().add(idMDInput);
        addMissileDestructor.getChildren().add(addMissileDestructorBtn);

        destroyMissile = new HBox();
        destroyMissile.setSpacing(10);
        destroyMissile.setPadding(new Insets(30,0,0,root.getWidth()/3));
        destroyMissile.getChildren().add(new Text("Attemt to Destroy Missile "));
        ComboBox<M> mList = new ComboBox<>();
        mList.setVisibleRowCount(10);
        mList.setMaxHeight(600);
        mList.setItems(FXCollections.observableArrayList(parsedEntities.getAllMissiles()));
        mList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mList.setItems(FXCollections.observableArrayList(parsedEntities.getAllMissiles()));
            }
        });
        destroyMissile.getChildren().add(mList);
        destroyMissile.getChildren().add(new Text("At "));
        TextField missileTimeValue = new TextField();
        missileTimeValue.setMaxWidth(35);
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
        destroyMissile.getChildren().add(missileTimeValue);
        destroyMissile.getChildren().add(new Text("seconds."));
        destroyMissile.getChildren().add(new Text("Source Destructor: "));
        ComboBox<MD> mdList = new ComboBox<>();
        // mldList.setVisibleRowCount(10);
        mdList.setMaxHeight(500);
        mdList.setPrefWidth(100);
        mdList.setItems(FXCollections.observableArrayList(parsedEntities.getMissileDestructors()));
        mdList.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mdList.setItems(FXCollections.observableArrayList(parsedEntities.getMissileDestructors()));
            }
        });
        destroyMissile.getChildren().add(mdList);
        Button addMCommand = new Button("Add Command");
        addMCommand.setOnAction(event -> {
            if (mdList.getValue() != null && mList.getValue() != null && !missileTimeValue.getText().equals("")) {


                DestMissile temp = new DestMissile(mList.getValue().getId(), missileTimeValue.getText());
                parsedEntities.addDestMissile(mdList.getValue().getId(), temp);

                toastMsg = "Destroy Missile Command " + temp.getId() + " was added...";
                Toast.makeText(window, toastMsg, msgType.LOG);
            } else {
                toastMsg = "Error in one of the 'Destroy Missile's fields";
                Toast.makeText(window, toastMsg, msgType.ERROR);
            }
        });

        destroyMissile.getChildren().add(addMCommand);


        addMissile = new HBox();
        addMissile.setSpacing(10);
        addMissile.getChildren().add(new Text("Add Missile: ID: "));
        Label m = new Label("M");
        m.setTranslateX(10);
        m.setFont(Font.font("Verdana", 20));
        TextField missileId = new TextField();

        missileId.setMaxWidth(35);
        missileId.textProperty().addListener(new ChangeListener<String>() {
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

        TextField launchTime = new TextField();
        launchTime.setMaxWidth(35);
        launchTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    launchTime.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(launchTime.getText().length() > 2)
                    launchTime.setText(oldValue);
            }
        });
        TextField flyTime = new TextField("2");
        flyTime.setMaxWidth(35);
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
        TextField damage = new TextField("1500");
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
        ComboBox<Target> targetsList = new ComboBox<>();
        targetsList.setMaxHeight(200);
        targetsList.setVisibleRowCount(10);
        targetsList.setItems(FXCollections.observableArrayList(parsedEntities.getTargets()));
        targetsList.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                targetsList.setItems(FXCollections.observableArrayList(parsedEntities.getTargets()));
            }
        });
        addMissile.getChildren().add(m);
        addMissile.getChildren().add(missileId);
        addMissile.getChildren().add(new Text("Target: "));
        addMissile.getChildren().add(targetsList);
        addMissile.getChildren().add(new Text("LaunchTime: "));
        addMissile.getChildren().add(launchTime);
        addMissile.getChildren().add(new Text("Fly Time: "));
        addMissile.getChildren().add(flyTime);
        addMissile.getChildren().add(new Text("Damage: "));
        addMissile.getChildren().add(damage);
        ComboBox<ML> launcherList = new ComboBox<>();
        launcherList.setVisibleRowCount(10);
        launcherList.setMaxHeight(200);
        launcherList.setItems(FXCollections.observableArrayList(parsedEntities.getMissileLaunchers()));
        launcherList.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                launcherList.setItems(FXCollections.observableArrayList(parsedEntities.getMissileLaunchers()));
            }
        });
        addMissile.getChildren().add(new Text("Source Launcher: "));
        addMissile.getChildren().add(launcherList);
        Button addMissileBtn = new Button("Add Missile");
        addMissileBtn.setOnAction(event -> {
            if (!missileId.getText().equals("") && targetsList.getValue() != null && !launchTime.getText().equals("") && !flyTime.getText().equals("") && !damage.getText().equals("") && launcherList.getValue() != null) {

                M temp = new M("M" + missileId.getText(), targetsList.getValue().toString(), launchTime.getText(), flyTime.getText(), damage.getText());
                boolean success = parsedEntities.addMissile(launcherList.getValue().toString(), temp);

                if (success) {
                    toastMsg = "Missile " + temp.getId() + " was added to Launcher " + launcherList.getValue().toString();
                    Toast.makeText(window, toastMsg, msgType.LOG);
                } else {
                    toastMsg = "Missile " + temp.getId() + " failed to load!!! ";
                    Toast.makeText(window, toastMsg, msgType.ERROR);
                }

            }
            else {
                toastMsg = "Error in one of the Create Missile's fields";
                Toast.makeText(window, toastMsg, msgType.ERROR);
            }

        });
        addMissile.getChildren().add(addMissileBtn);
        addMissile.setPadding(hboxPositions);


        Button submitBtn = new Button("Submit");
        submitBtn.setTranslateY(100);
        submitBtn.setTranslateX(root.getWidth()/2);
        submitBtn.setScaleX(2);
        submitBtn.setScaleY(2);
        submitBtn.setOnAction(event -> {

            scene2 = new Initialization(new VBox(), 1500, 948, parsedEntities, window, scene1, time);

            window.setScene(scene2);
        });





        root.getChildren().add(addLauncher);
        root.getChildren().add(addLauncherDestructor);
        root.getChildren().add(destroyLauncher);
        root.getChildren().add(addMissileDestructor);
        root.getChildren().add(destroyMissile);
        root.getChildren().add(addMissile);
        root.getChildren().add(submitBtn);



    }
}
