package com.afekawar.bl.base.Interface.Communication;

import com.afekawar.bl.base.Entities.MissileDestructor;

public class MissileDestructorEvent {
    private MissileDestructor source;

    public MissileDestructorEvent(MissileDestructor source){
        this.source = source;
    }

    public MissileDestructor getSource(){
        return source;
    }
}
