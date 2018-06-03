package UI.JSONParser.MockEntities;

import UI.JSONParser.MockEntities.BaseEntities.M;
import UI.JSONParser.MockEntities.BaseEntities.MD;
import UI.JSONParser.MockEntities.BaseEntities.ML;
import UI.JSONParser.MockEntities.BaseEntities.MLD;
import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;
import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;

import java.util.List;

public class War {
    private MissileLaunchers missileLaunchers;
    private MissileDestructors missileDestructors;
    private MissileLauncherDestructors missileLauncherDestructors;

    public War(){
        this.missileLaunchers = new MissileLaunchers();
        this.missileDestructors = new MissileDestructors();
        this.missileLauncherDestructors = new MissileLauncherDestructors();
    }

    public War(MissileLaunchers missileLaunchers, MissileDestructors missileDestructors, MissileLauncherDestructors missileLauncherDestructors) {
        this.missileLaunchers = missileLaunchers;
        this.missileDestructors = missileDestructors;
        this.missileLauncherDestructors = missileLauncherDestructors;
    }



    public List<ML> getMissileLaunchers() {
        return missileLaunchers.getLauncher();
    }

    public List<MD> getMissileDestructors() {
        return missileDestructors.getDestructors();
    }

    public List<MLD> getMissileLauncherDestructors() {
        return missileLauncherDestructors.getDestructor();
    }

    public boolean addMissileLauncher(ML temp){
        return  missileLaunchers.addMissileLauncher(temp);
    }
    public boolean addMissileLauncherDestructor(MLD temp){
        return missileLauncherDestructors.addMissileLauncherDestructor(temp);
    }
    public boolean addMissileDestructor(MD temp){
        return missileDestructors.addMissileDestructor(temp);
    }

    public void addDestLauncher(String destId, DestLauncher temp){
        missileLauncherDestructors.addDestLauncher(destId,temp);
    }
    public void addDestMissile(String destId, DestMissile temp){
        missileDestructors.addDestMissile(destId,temp);
    }
    public boolean addMissile(String launcherId, M temp){
        return missileLaunchers.addMissile(launcherId,temp);
    }

    public List<M> getAllMissiles(){
        return missileLaunchers.getAllMissiles();
    }

}
