package com.afekawar.bl.base.Interface;

import com.afekawar.bl.base.Entities.*;

public interface SystemInterface {
    public void addMissileLauncher(String id, MissileLauncher missileLauncher);
    public Runnable getEntityById(String id);
    public Thread getThreadById(String id);
    public void addMissileLauncherDestructor(String id, MissileLauncherDestructor missileLauncherDestructor);
    public void addMissileDestructor(String id, MissileDestructor missileDestructor);
    public void addMissile(String id, Missile missile);
    public void addTarget(String id, Target target);
    public Target getTargetById(String id);
    public void launchMissile(String destination, int damage, int flyTime);
    public void destroyMissileLauncher(String id);
    public void destroyMissile(String launcherId, String missleId);
    public void showStats();
    public void exit();


}
