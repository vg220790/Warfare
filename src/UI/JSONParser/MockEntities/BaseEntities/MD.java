package UI.JSONParser.MockEntities.BaseEntities;

import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;

import java.util.List;

public class MD {
    private String id;
    private List<DestMissile> destructdMissile;

    public String getId() {
        return id;
    }

    public List<DestMissile> getDestructdMissile() {
        return destructdMissile;
    }

    public MD(String id, List<DestMissile> destructdMissile) {
        this.id = id;
        this.destructdMissile = destructdMissile;
    }
}
