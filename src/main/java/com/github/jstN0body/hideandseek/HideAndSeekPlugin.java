package com.github.jstN0body.hideandseek;

import org.bukkit.plugin.java.JavaPlugin;

public class HideAndSeekPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new CompassCommands(this);
        new MinigamePrepCommands();
        new PlayerDeathListener(this);
    }
}
