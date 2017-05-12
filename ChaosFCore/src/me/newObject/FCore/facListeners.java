package me.newObject.FCore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;

public class facListeners implements Listener {
	
	FileConfiguration c = Core.getInstance().beacons.getConfig();
	
	@EventHandler
	public void onJoin(FPlayerJoinEvent e){
		if(c.contains(e.getFaction().getId())){
			int level = c.getInt(e.getFaction().getId() + ".level");
			if(level >= 2){
				Bukkit.dispatchCommand(Core.getInstance().getServer().getConsoleSender(), Core.getInstance().getConfig().getString("mcmmoBoost.Command").replace("{player}", e.getfPlayer().getPlayer().getName()));
				e.getfPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("recievedMcmmoBoost")));
			}else if(level == 3){
				Bukkit.dispatchCommand(Core.getInstance().getServer().getConsoleSender(), Core.getInstance().getConfig().getString("moneyBoost.Command").replace("{player}", e.getfPlayer().getPlayer().getName()));
				e.getfPlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("recievedMoneyBoost")));
			}
		}
	}
	@EventHandler
	public void onLeave(FPlayerLeaveEvent e){
		Bukkit.dispatchCommand(Core.getInstance().getServer().getConsoleSender(), Core.getInstance().getConfig().getString("mcmmoBoost.removeCommand").replace("{player}", e.getfPlayer().getPlayer().getName()));
		Bukkit.dispatchCommand(Core.getInstance().getServer().getConsoleSender(), Core.getInstance().getConfig().getString("moneyBoost.removeCommand").replace("{player}", e.getfPlayer().getPlayer().getName()));
	}
}
