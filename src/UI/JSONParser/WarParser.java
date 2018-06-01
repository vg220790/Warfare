package UI.JSONParser;

import UI.JSONParser.MockEntities.BaseEntities.MD;
import UI.JSONParser.MockEntities.BaseEntities.ML;
import UI.JSONParser.MockEntities.BaseEntities.MLD;
import UI.JSONParser.MockEntities.War;

import java.util.List;

public class WarParser {
    private War war;

    public WarParser(War war){
        this.war = war;
    }

    public List<ML> getMissileLaunchers(){
        return war.getMissileLaunchers();
    }
    public List<MD> getMissileDestructors(){
        return war.getMissileDestructors();
    }
    public List<MLD> getMissileLauncherDestructors(){
        return war.getMissileLauncherDestructors();
    }
}
