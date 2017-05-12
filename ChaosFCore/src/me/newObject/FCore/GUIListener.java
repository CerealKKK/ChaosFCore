package me.newObject.FCore;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

public class GUIListener implements Listener {

	FileConfiguration c = Core.getInstance().beacons.getConfig();
	HashMap<Player, Block> clickedBeacon = new HashMap<Player, Block>();
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Block b = e.getClickedBlock();
		Location l = b.getLocation();
		FPlayer fp = FPlayers.getInstance().getByPlayer(p);
		Faction f = fp.getFaction();
		if(c.contains(f.getId())){
			if(c.contains(f.getId() + ".loc")){
				if(Utils.str2loc(c.getString(f.getId() + ".loc")).equals(l)){
					if(!f.getFPlayerAdmin().equals(fp)){
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("needToBeFLeaderToUpgrade")));
						return;
					}
					clickedBeacon.put(e.getPlayer(), b);
					e.setCancelled(true);
					p.openInventory(Utils.GUI(c.getInt(f.getId() + ".level")));
				}
			}
		}else return;
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		if(!Utils.isGUI(e.getClickedInventory()))
			return;
		e.setCancelled(true);
		ItemStack clicked = e.getCurrentItem();
		if(clicked == null)
			return;
		if(!clicked.hasItemMeta())
			return;
		if(!clicked.getItemMeta().hasDisplayName())
			return;
		Player p = (Player) e.getWhoClicked();
		FPlayer fp = FPlayers.getInstance().getByPlayer(p);
		Faction f = fp.getFaction();
		if(!c.contains(f.getId())){
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("noFacGUIClick")));
			return;
		}
		if(!f.getFPlayerAdmin().equals(fp)){
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("needToBeFLeaderToUpgrade")));
			return;
		}
		int level = Core.getInstance().beacons.getConfig().getInt(f.getId() + ".level");
		if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("facPowerboost.Displayname")))){
			if(level != 0){
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("needToBeLevel").replace("{level}", 0 + "")));
				return;
			}
			Core.getInstance().beacons.getConfig().set(f.getId() + ".level", level+1);
			Core.getInstance().beacons.save();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Core.getInstance().getConfig().getString("facPowerboost.Command").replace("{facName}", f.getTag()));
			for(Player pl : f.getOnlinePlayers()){
				pl.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("recievedPowerBoost")));
			}
			
		}
		if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("moneyBoost.Displayname")))){
			if(level != 2){
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("needToBeLevel").replace("{level}", 2 +"")));
				return;
			}
			Core.getInstance().beacons.getConfig().set(f.getId() + ".level", level+1);
			Core.getInstance().beacons.save();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Core.getInstance().getConfig().getString("moneyBoost.Command"));
			for(Player pl : f.getOnlinePlayers()){
				pl.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("recievedMoneyBoost")));
			}
			for(FPlayer pl : f.getFPlayers()){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Core.getInstance().getConfig().getString("moneyBoost.Command").replace("{player}", pl.getName()));
			}
		}
		if(clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("mcmmoBoost.Displayname")))){
			if(level != 1){
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("needToBeLevel").replace("{level}", 1 + "")));
				return;
			}
			Core.getInstance().beacons.getConfig().set(f.getId() + ".level", level+1);
			Core.getInstance().beacons.save();
			
			for(Player pl : f.getOnlinePlayers()){
				pl.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("recievedMcmmoBoost")));
			}
			for(FPlayer pl : f.getFPlayers()){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Core.getInstance().getConfig().getString("mcmmoBoost.Command").replace("{player}", pl.getName()));
			}
		}

	}






}
