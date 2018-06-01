package UI.JSONParser.MockEntities.BaseEntities;

import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;

import java.util.List;

public class MLD {
    private String type;
    private List<DestLauncher> destructedLanucher;

    public MLD(String type, List<DestLauncher> destructedLanucher) {
        this.type = type;
        this.destructedLanucher = destructedLanucher;
    }

    public String getType() {

        return type;
    }

    public List<DestLauncher> getDestructedLanucher() {
        return destructedLanucher;
    }
}
