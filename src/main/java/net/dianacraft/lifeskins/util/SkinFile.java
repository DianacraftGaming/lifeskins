package net.dianacraft.lifeskins.util;

import java.util.ArrayList;
import java.util.List;

public class SkinFile {
    private List<Skin> skins = new ArrayList<>();
    private boolean slim;

    public SkinFile(boolean useSlim){
        slim = useSlim;
    }

    public boolean getSlim(){
        return slim;
    }

    public List<Skin> getSkinArray() {
        return skins;
    }

    public void setSkinArray(List<Skin> array){
        skins = array;
    }
}
