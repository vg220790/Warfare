package UI.JSONParser.MockEntities.BaseEntities;

import java.util.Objects;

public class M{
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

   @Override
   public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof M))
            return false;
       M otherM = (M)o;

       return this.id.equals(otherM.id);

   }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString(){
        return id;
    }
}
