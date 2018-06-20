package SharedInterface;


import com.afekawar.bl.base.Entities.BaseEntities.*;
import com.afekawar.bl.base.MainLogic;

import java.util.List;

public interface WarInterface {
    boolean addMissileLauncher(MissileLauncher temp);
    boolean addMissileLauncherDestructor(MissileLauncherDestructor temp);
    boolean addMissileDestructor(MissileDestructor temp);
    boolean addMissile(String launcherId, Missile temp);
    void addDestLauncher(String destId, String destLauncherId, int destTime);
    void addDestMissile(String destId, String destMissileId, int destTime);
    List<MissileLauncher> getMissileLaunchers();
    List<MissileDestructor> getMissileDestructors();
    List<MissileLauncherDestructor> getMissileLauncherDestructors();
    MissileLauncher getLauncherById(String id);
    List<Missile> getAllMissiles();
    List<Target> getTargets();
    Target getTargetByName(String name);
    void setMainProgram(MainLogic mainProgram);
    String showStats();
    void haltSystem();

}
