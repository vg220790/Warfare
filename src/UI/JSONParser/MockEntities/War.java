package UI.JSONParser.MockEntities;

import UI.JSONParser.MockEntities.BaseEntities.MD;
import UI.JSONParser.MockEntities.BaseEntities.ML;
import UI.JSONParser.MockEntities.BaseEntities.MLD;

import java.util.List;

public class War {
    private MissileLaunchers missileLaunchers;
    private MissileDestructors missileDestructors;

    public War(MissileLaunchers missileLaunchers, MissileDestructors missileDestructors, MissileLauncherDestructors missileLauncherDestructors) {
        this.missileLaunchers = missileLaunchers;
        this.missileDestructors = missileDestructors;
        this.missileLauncherDestructors = missileLauncherDestructors;
    }

    private MissileLauncherDestructors missileLauncherDestructors;

    public List<ML> getMissileLaunchers() {
        return missileLaunchers.getLauncher();
    }

    public List<MD> getMissileDestructors() {
        return missileDestructors.getDestructors();
    }

    public List<MLD> getMissileLauncherDestructors() {
        return missileLauncherDestructors.getDestructor();
    }
}
