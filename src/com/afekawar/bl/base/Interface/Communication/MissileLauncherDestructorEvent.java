package com.afekawar.bl.base.Interface.Communication;

import com.afekawar.bl.base.Entities.MissileLauncherDestructor;

public class MissileLauncherDestructorEvent {
    private MissileLauncherDestructor source;

    public MissileLauncherDestructorEvent(MissileLauncherDestructor source){
        this.source = source;
    }

    public MissileLauncherDestructor getSource(){
        return source;
    }
}
