package com.warsim.test;

public interface SystemInterface {

    public void AddMissileLauncher(String id, MissileLauncher missileLauncher);
    public void AddMissileLauncherDestructor(String id, MissileLauncherDestructor missileLauncherDestructor);
    public void AddMissileDestructor(String id, MissileDestructor missileDestructor);
    public void AddMissile(String id, Missile missile);
    public void LaunchMissile(String destination, int damage, int flyTime);
    public void DestroyMissileLauncher();
    public void DestroyMissile();
    public void ShowStats();
    public void Exit();
}
