package net.dianacraft.lifeskins.util;

public class SkinFile {
    private Skin[] skinArray;
    private boolean useSlim;

    public SkinFile(boolean slim){
        useSlim = slim;
    }

    public SkinFile(Skin[] skins, boolean slim){
        skinArray = skins;
        useSlim = slim;
    }

    public boolean getSlim(){
        return useSlim;
    }

    public Skin[] getSkinArray() {
        return skinArray;
    }
}
