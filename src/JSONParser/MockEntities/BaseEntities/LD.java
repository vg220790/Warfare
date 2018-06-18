package JSONParser.MockEntities.BaseEntities;

import JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;

import java.util.ArrayList;
import java.util.List;

public class LD {
    private static int idInc = 1;
    private String id;
    private String type;
    private List<DestLauncher> destructedLanucher;

    public LD(String type, List<DestLauncher> destructedLanucher) {
        this.id = "LD30" + idInc++;
        this.type = type;
        if(destructedLanucher == null)
            this.destructedLanucher = new ArrayList<>();
        else
            this.destructedLanucher = destructedLanucher;
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
