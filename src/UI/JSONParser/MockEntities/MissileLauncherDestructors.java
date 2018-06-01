package UI.JSONParser.MockEntities;

import UI.JSONParser.MockEntities.BaseEntities.MLD;

import java.util.List;

class MissileLauncherDestructors {
    private List<MLD> destructor;

    public MissileLauncherDestructors(List<MLD> destructor) {
        this.destructor = destructor;
    }

    List<MLD> getDestructor() {
        return destructor;
    }
}
