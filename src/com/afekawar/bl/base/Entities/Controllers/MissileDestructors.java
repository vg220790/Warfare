package com.afekawar.bl.base.Entities.Controllers;



import com.afekawar.bl.base.Entities.BaseEntities.Missile;
import com.afekawar.bl.base.Entities.BaseEntities.MissileDestructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MissileDestructors {
    private Set<MissileDestructor> destructor;

    public MissileDestructors(){
        this.destructor = new HashSet<>();
    }

    public MissileDestructors(List<MissileDestructor> destructor) {
        this.destructor = new HashSet<>(destructor);
    }

    public List<MissileDestructor> getDestructors(){
        return new ArrayList<>(destructor);
    }

    public boolean addMissileDestructor(MissileDestructor temp){
        int prevSize = destructor.size();
        destructor.add(temp);
        return !(destructor.size() == prevSize);
    }

    public void addDestMissile(String destId, Missile destMissile, int destTime){
        for(MissileDestructor dest : destructor){
            if(dest.getId().equals(destId)){
                dest.addTargetMissile(destTime,destMissile);
            }
        }
    }
}
