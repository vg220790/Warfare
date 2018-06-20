package com.afekawar.bl.base.Interface.Communication;

import com.afekawar.bl.base.Entities.BaseEntities.Missile;

public class MissileEvent {
    private Missile missile;

    public void setMissile(Missile missile){
        this.missile = missile;
    }
    public Missile getMissile(){
        return missile;
    }
}
