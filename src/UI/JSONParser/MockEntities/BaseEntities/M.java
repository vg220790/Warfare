package UI.JSONParser.MockEntities.BaseEntities;

public class M {
    private String id;
    private String destination;
    private String launchTime;
    private String flyTime;
    private String damage;

    public String getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public M(String id, String destination, String launchTime, String flyTime, String damage) {
        this.id = id;
        this.destination = destination;
        this.launchTime = launchTime;
        this.flyTime = flyTime;
        this.damage = damage;
    }

    public String getFlyTime() {

        return flyTime;
    }

    public String getDamage() {
        return damage;
    }
}
