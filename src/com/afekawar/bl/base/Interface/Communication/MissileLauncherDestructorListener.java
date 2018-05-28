package com.afekawar.bl.base.Interface.Communication;

import com.afekawar.bl.base.Entities.MissileLauncherDestructor;

public interface MissileLauncherDestructorListener {
    public void launchAntiMissileLauncher(MissileLauncherDestructorEvent e);
    public void destroyAntiMissileLauncher(MissileLauncherDestructorEvent e);
    public void createMissileLauncherDestructor(MissileLauncherDestructorEvent e);
}
