package UI.JSONParser.MockEntities;

import UI.JSONParser.MockEntities.BaseEntities.MD;

import java.util.List;

class MissileDestructors {
    private List<MD> destructor;

    public MissileDestructors(List<MD> destructor) {
        this.destructor = destructor;
    }

    List<MD> getDestructors(){
        return destructor;
    }
}
