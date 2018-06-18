package JSONParser.MockEntities.BaseEntities;

import JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MD {
    private String id;
    private List<DestMissile> destructdMissile;

    public String getId() {
        return id;
    }

    public void addDestMissile(DestMissile temp){
        destructdMissile.add(temp);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MD md = (MD) o;
        return Objects.equals(id, md.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public List<DestMissile> getDestructdMissile() {
        return destructdMissile;
    }

    public MD(String id, List<DestMissile> destructdMissile) {
        this.id = id;
        if(destructdMissile == null)
            this.destructdMissile = new ArrayList<>();
        else
            this.destructdMissile = destructdMissile;
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
