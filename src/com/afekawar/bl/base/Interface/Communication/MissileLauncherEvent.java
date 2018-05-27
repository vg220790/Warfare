package com.afekawar.bl.base.Interface.Communication;

import com.afekawar.bl.base.Entities.MissileLauncher;

public class MissileLauncherEvent {
    private MissileLauncher source;
    private String destroyedMissileId;

    public MissileLauncherEvent(MissileLauncher source){
        this.source = source;
    }
    public void setDestroyedMissileId(String destroyedMissileId){
        this.destroyedMissileId = destroyedMissileId;

    }
    public String getDestroyedMissileId(){
        return destroyedMissileId;
    }
    public MissileLauncher getSource(){
        return source;
    }
}
