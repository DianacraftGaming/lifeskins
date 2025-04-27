package net.dianacraft.lifeskins.util;

public class Skin {
    private String name;
    private int lives;
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

    public int getLifeCount() {
        return lives;
    }

    public String getName() {
        return name;
    }
}
