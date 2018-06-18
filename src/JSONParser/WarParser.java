package JSONParser;

import JSONParser.MockEntities.BaseEntities.M;
import JSONParser.MockEntities.BaseEntities.MD;
import JSONParser.MockEntities.BaseEntities.ML;
import JSONParser.MockEntities.BaseEntities.LD;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;
import JSONParser.MockEntities.War;
import SharedInterface.WarInterface;
import com.afekawar.bl.base.Entities.StaticTargets;
import com.afekawar.bl.base.Entities.Target;

import java.util.List;

public class WarParser implements WarInterface {
    private War war;
    private StaticTargets targets;

    public WarParser(){
        war = new War();
        targets = new StaticTargets();
    }
    public WarParser(War war){
       // scanner = new Scanner(System.in);
        this.war = war;
    }

    public List<ML> getMissileLaunchers(){
        return war.getMissileLaunchers();
    }
    public List<MD> getMissileDestructors(){
        return war.getMissileDestructors();
    }
    public List<LD> getMissileLauncherDestructors(){
        return war.getMissileLauncherDestructors();
    }

    public boolean addMissileLauncher(ML temp){
        return  war.addMissileLauncher(temp);
    }
    public boolean addMissileLauncherDestructor(LD temp){
        return war.addMissileLauncherDestructor(temp);
    }
    public boolean addMissileDestructor(MD temp){
        return war.addMissileDestructor(temp);
    }

    public boolean addDestLauncher(String destId, DestLauncher temp){
        if(!isLauncherDestructorExist(destId))
            return false;
        else {
            war.addDestLauncher(destId, temp);
            return true;
        }
    }
    public boolean addDestMissile(String destId, DestMissile temp){
        if(!isMissileDestructorExist(destId))
            return false;
        else{
            war.addDestMissile(destId,temp);
            return true;
        }

    }

    private boolean isLauncherDestructorExist(String id){
       for(LD dest : war.getMissileLauncherDestructors()){
           if(dest.getId().equals(id))
               return true;
       }
       return false;
    }
    private boolean isMissileDestructorExist(String id){
        for(MD dest : war.getMissileDestructors()){
            if(dest.getId().equals(id))
                return true;
        }
        return false;
    }
    @Override
    public void showStats() {

    }

    @Override
    public void haltSystem() {

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


    @Override
    public void launchAntiLauncherMissile() {

    }

    public List<M> getAllMissiles(){
        return war.getAllMissiles();
    }


}


