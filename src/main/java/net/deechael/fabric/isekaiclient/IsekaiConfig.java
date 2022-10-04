package net.deechael.fabric.isekaiclient;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "isekaiclient")
public class IsekaiConfig implements ConfigData {

    public boolean removeBreakingCooldown = false;
    public boolean highlightWanderingTrader = false;
    public boolean avoidIntentionalGameDesign = false;

}
