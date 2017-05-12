package me.newObject.FCore;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;



public class Core extends JavaPlugin implements Listener {
	
	Config beacons;
	
	
	public static Core instance;
	public static Core getInstance(){
		return instance;
	}
	
	public void onEnable(){
		instance = this;
		beacons = new Config(this, "", "beacons");
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		reloadConfig();
		getServer().getPluginManager().registerEvents(new facListeners(), this);
		getServer().getPluginManager().registerEvents(new placeBreakListener(), this);
		getServer().getPluginManager().registerEvents(new GUIListener(), this);
		getCommand("fcore").setExecutor(new fcoreCMD());
		
		
	}
	
	
	
	
	
}
