package UI.JSONParser;

import UI.JSONParser.MockEntities.BaseEntities.M;
import UI.JSONParser.MockEntities.BaseEntities.MD;
import UI.JSONParser.MockEntities.BaseEntities.ML;
import UI.JSONParser.MockEntities.BaseEntities.MLD;
import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;
import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;
import UI.JSONParser.MockEntities.War;
import com.afekawar.bl.base.Entities.StaticTargets;
import com.afekawar.bl.base.Entities.Target;

import java.util.List;

public class WarParser {
    private War war;
    private StaticTargets targets;

    public WarParser(){
        war = new War();
        targets = new StaticTargets();
    }
    public WarParser(War war){
        this.war = war;
    }

    public List<ML> getMissileLaunchers(){
        return war.getMissileLaunchers();
    }
    public List<MD> getMissileDestructors(){
        return war.getMissileDestructors();
    }
    public List<MLD> getMissileLauncherDestructors(){
        return war.getMissileLauncherDestructors();
    }

    public boolean addMissileLauncher(ML temp){
        return  war.addMissileLauncher(temp);
    }
    public boolean addMissileLauncherDestructor(MLD temp){
        return war.addMissileLauncherDestructor(temp);
    }
    public boolean addMissileDestructor(MD temp){
        return war.addMissileDestructor(temp);
    }

    public void addDestLauncher(String destId, DestLauncher temp){
        war.addDestLauncher(destId,temp);
    }
    public void addDestMissile(String destId, DestMissile temp){
        war.addDestMissile(destId,temp);
    }
    public List<Target> getTargets(){
        return targets.getTargets();
    }
    public boolean addMissile(String launcherId, M temp){
        for(M t : getAllMissiles()){
            if (t.equals(temp))
                return false;
        }
        return war.addMissile(launcherId,temp);
    }
    public List<M> getAllMissiles(){
        return war.getAllMissiles();
    }


}


