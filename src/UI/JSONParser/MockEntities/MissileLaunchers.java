package UI.JSONParser.MockEntities;

import UI.JSONParser.MockEntities.BaseEntities.ML;

import java.util.List;


class MissileLaunchers {
    private List<ML> launcher;

    public MissileLaunchers(List<ML> launcher) {
        this.launcher = launcher;
    }

    List<ML> getLauncher() {
        return launcher;
    }
}


