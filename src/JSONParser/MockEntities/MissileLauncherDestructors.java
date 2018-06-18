package JSONParser.MockEntities;

import JSONParser.MockEntities.BaseEntities.LD;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MissileLauncherDestructors {
    private Set<LD> destructor;

    public MissileLauncherDestructors(){
        this.destructor = new HashSet<>();
    }

    public MissileLauncherDestructors(List<LD> destructor) {
        this.destructor = new HashSet<>(destructor);
    }

    List<LD> getDestructor() {
        return new ArrayList<>(destructor);
    }

    public boolean addMissileLauncherDestructor(LD temp){
        int prevSize = destructor.size();
        destructor.add(temp);
        return !(destructor.size() == prevSize);
    }

    public void addDestLauncher(String destId, DestLauncher temp){
        for(LD dest : destructor){
            if(dest.getId().equals(destId)){
                dest.addDestLauncher(temp);
            }
        }
    }
}



