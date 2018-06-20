package com.afekawar.bl.base.Entities;


// Max Golotsvan - 314148123
// Maksim Lyashenko - 317914877

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
    public boolean addMissile(String launcherId, Missile temp){
        return missileLaunchers.addMissile(launcherId,temp);
    }

    public List<Missile> getAllMissiles(){
        return missileLaunchers.getAllMissiles();
    }

}
