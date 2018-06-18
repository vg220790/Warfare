package JSONParser.MockEntities.BaseEntities.SubEntities;

public class DestLauncher {
    private String id;
    private String destructTime;

    public DestLauncher(String id, String destructTime) {
        this.id = id;
        this.destructTime = destructTime;
    }

    public String getId() {
        return id;
    }

    public String getDestructTime() {
        return destructTime;
    }
}

