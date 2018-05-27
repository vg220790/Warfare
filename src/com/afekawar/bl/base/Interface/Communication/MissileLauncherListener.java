package com.afekawar.bl.base.Interface.Communication;

public interface MissileLauncherListener {

    public void launcherCreated(MissileLauncherEvent e);
    public void launcherDestroyed(MissileLauncherEvent e);
    public void launchMissile(MissileLauncherEvent e);
    public void destroyMissile(MissileLauncherEvent e);
    public void hideMissileLauncher(MissileLauncherEvent e);

}
