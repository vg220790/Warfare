package SharedInterface;

import com.afekawar.bl.base.Entities.BaseEntities.*;
import com.afekawar.bl.base.Entities.War;
import com.afekawar.bl.base.MainLogic;


import java.util.List;

public class WarImp implements WarInterface {
    private War war;
    private StaticTargets targets;
    private transient MainLogic mainProgram;

    public WarImp(){
        war = new War();
        targets = new StaticTargets();
    }

    @Override
    public void setMainProgram(MainLogic mainProgram){
        this.mainProgram = mainProgram;
    }

    @Override
    public List<MissileLauncher> getMissileLaunchers(){
        return war.getMissileLaunchers();
    }
    @Override
    public List<MissileDestructor> getMissileDestructors(){
        return war.getMissileDestructors();
    }
    @Override
    public List<MissileLauncherDestructor> getMissileLauncherDestructors(){
        return war.getMissileLauncherDestructors();
    }

    @Override
    public boolean addMissileLauncher(MissileLauncher temp){
        return  war.addMissileLauncher(temp);
    }
    @Override
    public boolean addMissileLauncherDestructor(MissileLauncherDestructor temp){
        return war.addMissileLauncherDestructor(temp);
    }
    @Override
    public boolean addMissileDestructor(MissileDestructor temp){
        return war.addMissileDestructor(temp);
    }

    @Override
    public boolean addDestLauncher(String destId, String destLauncherId, int destTime){
        mainProgram.addDestLauncherCommand(destId,destTime,destLauncherId);
        return true;
    }
    @Override
    public boolean addDestMissile(String destId, String destMissileId, int destTime){
        mainProgram.addDestMissileCommand(destId,destTime,destMissileId);
        return true;
    }

    @Override
    public void showStats() {

    }

    @Override
    public void haltSystem() {

    }

    @Override
    public MissileLauncher getLauncherById(String id){
        for(MissileLauncher launcher : getMissileLaunchers()){
            if(launcher.getId().equals(id))
                return launcher;
        }
        return null;
    }


    @Override
    public List<Target> getTargets(){
        return targets.getTargets();
    }

    @Override
    public Target getTargetByName(String name){
        return targets.getTargetByName(name);
    }

    @Override
    public boolean addMissile(String launcherId, Missile temp){
        for(Missile t : getAllMissiles()){
            if (t.equals(temp))
                return false;
        }
        return war.addMissile(launcherId,temp);
    }


    @Override
    public void launchAntiLauncherMissile() {

    }

    @Override
    public List<Missile> getAllMissiles(){
        return war.getAllMissiles();
    }


}


