package SharedInterface;


import JSONParser.MockEntities.BaseEntities.M;
import JSONParser.MockEntities.BaseEntities.MD;
import JSONParser.MockEntities.BaseEntities.ML;
import JSONParser.MockEntities.BaseEntities.LD;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;

public interface WarInterface {
    public boolean addMissileLauncher(ML temp);
    public boolean addMissileLauncherDestructor(LD temp);
    public boolean addMissileDestructor(MD temp);
    public boolean addMissile(String launcherId, M temp);
    public void launchAntiLauncherMissile();
    public boolean addDestLauncher(String destId, DestLauncher temp);
    public boolean addDestMissile(String destId, DestMissile temp);
    public void showStats();
    public void haltSystem();
}
