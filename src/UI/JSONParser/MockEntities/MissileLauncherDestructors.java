package UI.JSONParser.MockEntities;

import UI.JSONParser.MockEntities.BaseEntities.MD;
import UI.JSONParser.MockEntities.BaseEntities.MLD;
import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MissileLauncherDestructors {
    private Set<MLD> destructor;

    public MissileLauncherDestructors(){
        this.destructor = new HashSet<>();
    }

    public MissileLauncherDestructors(List<MLD> destructor) {
        this.destructor = new HashSet<>(destructor);
    }

    List<MLD> getDestructor() {
        return new ArrayList<>(destructor);
    }

    public boolean addMissileLauncherDestructor(MLD temp){
        int prevSize = destructor.size();
        destructor.add(temp);
        return !(destructor.size() == prevSize);
    }

    public void addDestLauncher(String destId, DestLauncher temp){
        for(MLD dest : destructor){
            if(dest.getId().equals(destId)){
                dest.addDestLauncher(temp);
            }
        }
    }
}



