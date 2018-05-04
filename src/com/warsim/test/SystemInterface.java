package com.warsim.test;

public interface SystemInterface {

    public void addMissileLauncher(String id, MissileLauncher missileLauncher);
    public void addMissileLauncherDestructor(String id, MissileLauncherDestructor missileLauncherDestructor);
    public void addMissileDestructor(String id, MissileDestructor missileDestructor);
    public void addMissile(String id, Missile missile);
    public void launchMissile(String destination, int damage, int flyTime);
    public void destroyMissileLauncher();
    public void destroyMissile();
    public void showStats();
    public void exit();
}
