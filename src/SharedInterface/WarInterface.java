package SharedInterface;


import com.afekawar.bl.base.Entities.BaseEntities.*;
import com.afekawar.bl.base.MainLogic;

import java.util.List;

public interface WarInterface {
    public boolean addMissileLauncher(MissileLauncher temp);
    public boolean addMissileLauncherDestructor(MissileLauncherDestructor temp);
    public boolean addMissileDestructor(MissileDestructor temp);
    public boolean addMissile(String launcherId, Missile temp);
    public void launchAntiLauncherMissile();
    public boolean addDestLauncher(String destId, String destLauncherId, int destTime);
    public boolean addDestMissile(String destId, String destMissileId, int destTime);
    public List<MissileLauncher> getMissileLaunchers();
    public List<MissileDestructor> getMissileDestructors();
    public List<MissileLauncherDestructor> getMissileLauncherDestructors();
    public MissileLauncher getLauncherById(String id);
    public List<Missile> getAllMissiles();
    public List<Target> getTargets();
    public Target getTargetByName(String name);
    public void setMainProgram(MainLogic mainProgram);
    public void showStats();
    public void haltSystem();

}
