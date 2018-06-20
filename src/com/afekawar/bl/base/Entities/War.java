package com.afekawar.bl.base.Entities;




import com.afekawar.bl.base.Entities.BaseEntities.Missile;
import com.afekawar.bl.base.Entities.BaseEntities.MissileDestructor;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncher;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncherDestructor;
import com.afekawar.bl.base.Entities.Controllers.MissileDestructors;
import com.afekawar.bl.base.Entities.Controllers.MissileLauncherDestructors;
import com.afekawar.bl.base.Entities.Controllers.MissileLaunchers;

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



    public List<MissileLauncher> getMissileLaunchers() {
        return missileLaunchers.getLauncher();
    }

    public List<MissileDestructor> getMissileDestructors() {
        return missileDestructors.getDestructors();
    }

    public List<MissileLauncherDestructor> getMissileLauncherDestructors() {
        return missileLauncherDestructors.getDestructor();
    }

    public boolean addMissileLauncher(MissileLauncher temp){
        return  missileLaunchers.addMissileLauncher(temp);
    }
    public boolean addMissileLauncherDestructor(MissileLauncherDestructor temp){
        return missileLauncherDestructors.addMissileLauncherDestructor(temp);
    }
    public boolean addMissileDestructor(MissileDestructor temp){
        return missileDestructors.addMissileDestructor(temp);
    }

    public void addDestLauncher(String destId, MissileLauncher destLauncher, int destTime){

        missileLauncherDestructors.addDestLauncher(destId,destLauncher,destTime);
    }
    public void addDestMissile(String destId, Missile destMissile, int destTime){
        missileDestructors.addDestMissile(destId,destMissile,destTime);
    }
    public boolean addMissile(String launcherId, Missile temp){
        return missileLaunchers.addMissile(launcherId,temp);
    }

    public List<Missile> getAllMissiles(){
        return missileLaunchers.getAllMissiles();
    }

}
