package UI.JSONParser.MockEntities.BaseEntities;

import java.util.*;

public class ML {
    private String id;
    private boolean isHidden;
    private Set<M> missile;

    public ML(String id, boolean isHidden, List<M> missile) {
        this.id = id;
        this.isHidden = isHidden;
        if(missile == null)
            this.missile = new HashSet<>();
        else
            this.missile = new HashSet<M>(missile);
    }

    public String getId() {

        return id;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public List<M> getMissile() { return new ArrayList<M>(missile);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ML ml = (ML) o;
        return Objects.equals(id, ml.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public boolean addMissile(M temp){
        int prevSize = missile.size();
        missile.add(temp);
        return !(missile.size() == prevSize);
    }

    @Override
    public String toString(){
        return id;
    }
}


