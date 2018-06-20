package com.afekawar.bl.base;

public class Statistics {
    private int launchersDestroyed;
    private int missilesDestroyed;
    private int missilesLaunched;
    private int missilesReachedDestination;
    private int totatDamage;
    public Statistics(){
        launchersDestroyed = 0;
        missilesDestroyed = 0;
        missilesLaunched = 0;
        missilesReachedDestination = 0;
        totatDamage = 0;
    }

    public int getLaunchersDestroyed() {
        return launchersDestroyed;
    }

    public void addDestroyedLauncher() {
        this.launchersDestroyed++;
    }

    public int getMissilesDestroyed() {
        return missilesDestroyed;
    }

    public void addDestroyedMissile() {
        this.missilesDestroyed++;
    }

    public int getMissilesLaunched() {
        return missilesLaunched;
    }

    public void addLaunchedMissile() {
        this.missilesLaunched++;
    }

    public int getMissilesReachedDestination() {
        return missilesReachedDestination;
    }

    public void addMissileReachedDestination() {
        this.missilesReachedDestination++;
    }

    public int getTotatDamage() {
        return totatDamage;
    }

    public void addDamage(int damage) {
        this.totatDamage += damage;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("Missiles Launched: " + missilesLaunched + ".\n");
        sb.append("Missiles Destroyed: " + missilesDestroyed + ".\n");
        sb.append("Missiles Reached Destination: " + missilesReachedDestination + ".\n");
        sb.append("Launchers Destroyed: " + launchersDestroyed + ".\n");
        sb.append("Total Damage: " + totatDamage + "$.\n");
        return sb.toString();
    }
}
