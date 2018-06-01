package UI.JSONParser.MockEntities.BaseEntities;

import java.util.List;

public class ML {
    private String id;
    private boolean isHidden;
    private List<M> missile;

    public ML(String id, boolean isHidden, List<M> missile) {
        this.id = id;
        this.isHidden = isHidden;
        this.missile = missile;
    }

    public String getId() {

        return id;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public List<M> getMissile() {
        return missile;
    }
}
