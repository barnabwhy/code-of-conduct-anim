package cc.barnab.codeOfConductAnim;

import net.fabricmc.api.ModInitializer;

public class CodeOfConductAnim implements ModInitializer {

    @Override
    public void onInitialize() {
        AnimatedCodeOfConduct.loadFrames();
    }
}
