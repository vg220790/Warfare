package com.warsim.test;

public interface SystemInterface {

    public void AddMissleLauncher(String id, MissleLauncher missleLauncher);
    public void AddMissleLauncherDestructor(String id, MissleLauncherDestructor missleLauncherDestructor);
    public void AddMissleDestructor(String id, MissleDestructor missleDestructor);
    public void LaunchMissle(String destination, int damage, int flyTime);
    public void DestroyMissleLauncher();
    public void DestroyMissle();
    public void ShowStats();
    public void Exit();
}
