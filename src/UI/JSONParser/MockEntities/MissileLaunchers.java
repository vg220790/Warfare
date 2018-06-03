package UI.JSONParser.MockEntities;

import UI.JSONParser.MockEntities.BaseEntities.M;
import UI.JSONParser.MockEntities.BaseEntities.ML;
import com.afekawar.bl.base.Entities.MissileLauncher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


class MissileLaunchers {
    private Set<ML> launcher;

    public MissileLaunchers(){
        this.launcher = new HashSet<>();
    }

    public MissileLaunchers(List<ML> launcher) {
        this.launcher = new HashSet<>(launcher);
    }

    List<ML> getLauncher() {
        return new ArrayList<>(launcher);
    }

    public boolean addMissileLauncher(ML temp){
        int prevSize = launcher.size();
        launcher.add(temp);
        return !(launcher.size() == prevSize);
    }

    public boolean addMissile(String launcherId, M temp){
        for(ML mLauncher : launcher ){
            if(mLauncher.getId().equals(launcherId)){
                return mLauncher.addMissile(temp);
            }
        }
        return false;
    }

    public List<M> getAllMissiles(){
        List<M> temp = new ArrayList<>();
        for(ML ml : launcher){
            temp.addAll(ml.getMissile());
        }
        return temp;
    }
}



