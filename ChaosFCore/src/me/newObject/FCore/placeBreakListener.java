package me.newObject.FCore;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

public class placeBreakListener implements Listener {

	FileConfiguration c = Core.getInstance().beacons.getConfig();

	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		ItemStack placed = e.getItemInHand();
		if(!Utils.isBeacon(placed))
			return;
		FPlayer fp = FPlayers.getInstance().getByPlayer(e.getPlayer());
		Player p = e.getPlayer();
		if(!fp.hasFaction()){
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("needFactionToPlace")));
			e.setCancelled(true);
			return;
		}
		if(!fp.getFaction().getFPlayerAdmin().equals(fp)){
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("needToBeFLeaderToPlace")));
			e.setCancelled(true);
			return;
		}
		Location l = e.getBlock().getLocation();
		FLocation flocOfBlock = new FLocation(l);
		Faction faction = Board.getInstance().getFactionAt(flocOfBlock);
		Faction f = fp.getFaction();
		if(faction.isNone()){
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("cantPlaceInOwnFaction")));
			e.setCancelled(true);
			return;
		}
		if(f != faction){
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("cantPlaceInOwnFaction")));
			e.setCancelled(true);
			return;
		}
		if(c.contains(f.getId() + ".loc")){
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("facAlreadyHasBeacon")));
			e.setCancelled(true);
			return;
		}
		
		List<String> lores = placed.getItemMeta().getLore();
		int level = 0;
		try{
			level = Integer.parseInt(lores.get(lores.size()-1).substring(lores.get(lores.size()-1).length()-1));
		}catch(NumberFormatException ex){}
		
		c.set(f.getId() + ".level", level);
		c.set(f.getId() + ".loc", Utils.loc2str(l));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("placedBeaconDown")));
		Core.getInstance().beacons.save();
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(e.getBlock().getType() != Material.BEACON)
			return;
		FPlayer fp = FPlayers.getInstance().getByPlayer(e.getPlayer());
		Player p = e.getPlayer();
		Location l = e.getBlock().getLocation();
		Faction f = fp.getFaction();
		if(c.contains(f.getId() + ".loc")){
			if(Utils.str2loc(c.getString(f.getId() + ".loc")).equals(l)){
				ItemStack is = Utils.getBeacon();
				ItemMeta im = is.getItemMeta();
				List<String> lores = im.getLore();
				lores.set(lores.size()-1, ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("beaconLevelLore").replace("{level}", "" + c.getInt(f.getId() + ".level"))));
				im.setLore(lores);
				is.setItemMeta(im);
				l.getWorld().dropItem(l, is);
				c.set(f.getId(), null);
				Core.getInstance().beacons.save();
			}
		}
	}
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e){
		for(Block b : e.blockList()){
			if(b.getType() == Material.BEACON){
				Location l = b.getLocation();
				FLocation flocOfBlock = new FLocation(l);
				Faction f = Board.getInstance().getFactionAt(flocOfBlock);
				if(c.contains(f.getId())){
					if(Utils.str2loc(c.getString(f.getId() + ".loc")).equals(l)){
						c.set(f.getId(), null);
						Core.getInstance().beacons.save();
					}
				}
			}
		}
	}
	
}
