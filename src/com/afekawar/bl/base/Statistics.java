package com.afekawar.bl.base;

// Max Golotsvan - 314148123
// Maksim Lyashenko - 317914877

public class Statistics {
    private int launchersDestroyed;
    private int missilesDestroyed;
    private int missilesLaunched;
    private int missilesReachedDestination;
    private int totatDamage;
    Statistics(){
        launchersDestroyed = 0;
        missilesDestroyed = 0;
        missilesLaunched = 0;
        missilesReachedDestination = 0;
        totatDamage = 0;
    }


    public void addDestroyedLauncher() {
        this.launchersDestroyed++;
    }
    public void addDestroyedMissile() {
        this.missilesDestroyed++;
    }
    public void addLaunchedMissile() {
        this.missilesLaunched++;
    }
    public void addMissileReachedDestination() {
        this.missilesReachedDestination++;
    }
    public void addDamage(int damage) {
        this.totatDamage += damage;
    }
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("Missiles Launched: ");
        sb.append(missilesLaunched);
        sb.append(".\n");
        sb.append("Missiles Destroyed: ");
        sb.append(missilesDestroyed);
        sb.append(".\n");
        sb.append("Missiles Reached Destination: ");
        sb.append(missilesReachedDestination);
        sb.append(".\n");
        sb.append("Launchers Destroyed: ");
        sb.append(launchersDestroyed);
        sb.append(".\n");
        sb.append("Total Damage: ");
        sb.append(totatDamage);
        sb.append(".\n");
        return sb.toString();
    }
}
