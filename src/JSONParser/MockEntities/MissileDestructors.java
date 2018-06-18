package JSONParser.MockEntities;

import JSONParser.MockEntities.BaseEntities.MD;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MissileDestructors {
    private Set<MD> destructor;

    public MissileDestructors(){
        this.destructor = new HashSet<>();
    }

    public MissileDestructors(List<MD> destructor) {
        this.destructor = new HashSet<>(destructor);
    }

    List<MD> getDestructors(){
        return new ArrayList<>(destructor);
    }

    public boolean addMissileDestructor(MD temp){
        int prevSize = destructor.size();
        destructor.add(temp);
        return !(destructor.size() == prevSize);
    }

    public void addDestMissile(String destId, DestMissile temp){
        for(MD dest : destructor){
            if(dest.getId().equals(destId)){
                dest.addDestMissile(temp);
            }
        }
    }
}
