package com.afekawar.bl.base.Interface.Communication;

public interface MissileDestructorListener {
    public void createMissileDestructor(MissileDestructorEvent e);
    public void launchAntiMissile(MissileDestructorEvent e);
    public void destroyAntiMissile(MissileDestructorEvent e);

}
