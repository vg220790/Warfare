package com.afekawar.bl.base.Entities.Controllers;

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

    public List<MissileDestructor> getDestructors(){
        return new ArrayList<>(destructor);
    }

    public boolean addMissileDestructor(MissileDestructor temp){
        int prevSize = destructor.size();
        destructor.add(temp);
        return !(destructor.size() == prevSize);
    }

}
