package net.dianacraft.lifeskins.util;

public class Skin {
    private String skinName;
    private int lifeCount;
    private boolean useSlim;

    public Skin(String name, int lives){
        skinName = name;
        lifeCount = lives;
    }

    public Skin(String name, int lives, boolean slim){
        skinName = name;
        lifeCount = lives;
        useSlim = slim;
    }

    public boolean getSlim(){
        return useSlim;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public String getSkinName() {
        return skinName;
    }
}
