package me.newObject.FCore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class fcoreCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("fcore")){
			if(!(sender instanceof Player))
				return false;
			Player p = (Player) sender;
			if(p.getInventory().contains(Utils.getBeacon()))
				return true;
			p.getInventory().addItem(Utils.getBeacon());
		    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("recievedBeaconMsg")));
			return true;
		}
		return false;
	}
	

	
	
	
	
	
	
	
}