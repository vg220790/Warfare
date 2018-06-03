package UI.JSONParser.MockEntities.BaseEntities;

import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MLD {
    private static int idInc = 1;
    private String id;
    private String type;
    private List<DestLauncher> destructedLanucher;

    public MLD(String type, List<DestLauncher> destructedLanucher) {
        this.id = "MLD30" + idInc++;
        this.type = type;
        if(destructedLanucher == null)
            this.destructedLanucher = new ArrayList<>();
        else
            this.destructedLanucher = destructedLanucher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MLD mld = (MLD) o;
        return Objects.equals(id, mld.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public void addDestLauncher(DestLauncher temp){
        destructedLanucher.add(temp);
    }

    public String getType() {

        return type;
    }

    public List<DestLauncher> getDestructedLanucher() {
        return destructedLanucher;
    }

    @Override
    public String toString(){
        return id;
    }

    public String getId(){
        return id;
    }
}
