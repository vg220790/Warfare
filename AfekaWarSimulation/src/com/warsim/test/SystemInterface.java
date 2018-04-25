package com.warsim.test;

public interface SystemInterface {

    public void AddMissleLauncher();
    public void AddMissleLauncherDestructor();
    public void AddMissleDestructor();
    public void LaunchMissle(String destination, int damage, int flyTime);
    public void DestroyMissleLauncher();
    public void DestroyMissle();
    public void ShowStats();
}
