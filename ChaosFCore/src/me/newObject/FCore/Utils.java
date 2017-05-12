package me.newObject.FCore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Utils {
	
	static FileConfiguration config = Core.getInstance().getConfig();
	public static ItemStack getBeacon(){
		ItemStack is = new ItemStack(Material.BEACON);
		ItemMeta ism = is.getItemMeta();
		ism.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("beaconDisplayName")));
		List<String> lores = new ArrayList<String>();
		if(config.getStringList("beaconLores").size() > 0){
			for(String s : config.getStringList("beaconLores")){
				lores.add(ChatColor.translateAlternateColorCodes('&', s));
			}
			lores.add(ChatColor.translateAlternateColorCodes('&', config.getString("beaconLevelLore").replace("{level}", "0")));
			ism.setLore(lores);
		}
		
		is.setItemMeta(ism);
		return is;
	}
	public static Boolean isBeacon(ItemStack i){
		if(i == null)
			return false;
		if(i.getType() != getBeacon().getType())
			return false;
		if(!i.hasItemMeta())
			return false;
		if(!i.getItemMeta().hasDisplayName())
			return false;
		if(!i.getItemMeta().hasLore())
			return false;
		if(!i.getItemMeta().getDisplayName().equalsIgnoreCase(getBeacon().getItemMeta().getDisplayName()))
			return false;
		if(!i.getItemMeta().getLore().get(0).equalsIgnoreCase(getBeacon().getItemMeta().getLore().get(0)))
			return false;
		
		return true;
	}
	public static Location str2loc(String str)
	{
		String[] str2loc = str.split("\\:");
		Location loc = new Location(Core.getInstance().getServer().getWorld(str2loc[0]), 0.0D, 0.0D, 0.0D);
		loc.setX(Double.parseDouble(str2loc[1]));
		loc.setY(Double.parseDouble(str2loc[2]));
		loc.setZ(Double.parseDouble(str2loc[3]));
		return loc;
	}

	public static String loc2str(Location loc)
	{
		return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
	}
	/*
		fac/powerboost
		mcmmoboost
		moneyboost
		
		
		
		
	*/
	public static Inventory GUI(int lvl){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', config.getString("guiTitle")));
		int level = lvl;
		
		
		for(int i = 0; i<27; i++){
			inv.setItem(i, createStack(Material.STAINED_GLASS_PANE, ChatColor.translateAlternateColorCodes('&', "&9Level&f: &e" + level), (byte) 0, new ArrayList<String>()));
		}
		ItemStack pb = createStack(Material.getMaterial(Core.getInstance().getConfig().getString("facPowerboost.Material")), 
				ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("facPowerboost.Displayname")), (byte) 1, Core.getInstance().getConfig().getStringList("facPowerboost.Lore"));
		ItemStack mb = createStack(Material.getMaterial(Core.getInstance().getConfig().getString("mcmmoBoost.Material")), 
				ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("mcmmoBoost.Displayname")), (byte) 1, Core.getInstance().getConfig().getStringList("mcmmoBoost.Lore"));
		ItemStack mob = createStack(Material.getMaterial(Core.getInstance().getConfig().getString("moneyBoost.Material")), 
				ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("moneyBoost.Displayname")), (byte) 1, Core.getInstance().getConfig().getStringList("moneyBoost.Lore"));
		inv.setItem(12, pb);
		inv.setItem(13, mb);
		inv.setItem(14, mob);
		
		return inv;
	}
	public static ItemStack createStack(Material m, String name, byte data, List<String> lores){
		ItemStack is = new ItemStack(m, 1, data);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lores);
		is.setItemMeta(im);
		return is;
	}
	public static Boolean isGUI(Inventory inv){
		if(inv == null)
			return false;
		if(inv.getName() == null)
			return false;
		if(!inv.getName().equalsIgnoreCase(GUI(0).getName()))
			return false;
		return true;
	}
}
