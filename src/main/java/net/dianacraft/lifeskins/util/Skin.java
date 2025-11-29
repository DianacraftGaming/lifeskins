package net.dianacraft.lifeskins.util;

public class Skin {
    private String name;
    private String team;
    private Integer lives;
    private boolean slim;

    public Skin(String skinName, int lifeCount){
        name = skinName;
        lives = lifeCount;
    }

    public Skin(String skinName, int lifeCount, boolean useSlim){
        name = skinName;
        lives = lifeCount;
        slim = useSlim;
    }

    public boolean getSlim(){
        return slim;
    }

    public Integer getLifeCount() {
        return lives;
    }

    //public Skin setLifeCount(int lifeCount){ return new Skin(name, lifeCount, slim); }

    public String getName() {
        return name;
    }

    public String getTeam() { return team; }
}
