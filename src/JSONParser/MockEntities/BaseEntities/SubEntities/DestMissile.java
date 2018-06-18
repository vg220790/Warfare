package JSONParser.MockEntities.BaseEntities.SubEntities;

public class DestMissile {
    private String id;

    public DestMissile(String id, String destructAfterLaunch) {
        this.id = id;
        this.destructAfterLaunch = destructAfterLaunch;
    }

    private String destructAfterLaunch;

    public String getId() {
        return id;
    }

    public String getDestructAfterLaunch() {
        return destructAfterLaunch;
    }
}
