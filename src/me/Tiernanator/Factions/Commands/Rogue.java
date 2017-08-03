package me.Tiernanator.Factions.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Tiernanator.Factions.FactionsMain;
import me.Tiernanator.Factions.Factions.Faction;
import me.Tiernanator.Factions.Factions.FactionAccessor;
import me.Tiernanator.Utilities.Colours.Colour;

public class Rogue implements CommandExecutor {

	@SuppressWarnings("unused")
	private static FactionsMain plugin;

	ChatColor warning = Colour.ALTERNATE_WARNING.getColour();
	ChatColor informative = Colour.ALTERNATE_INFORMATIVE.getColour();
	ChatColor highlight = Colour.ALTERNATE_HIGHLIGHT.getColour();
	ChatColor good = Colour.ALTERNATE_GOOD.getColour();
	ChatColor regal = Colour.REGAL.getColour();
	
	public Rogue(FactionsMain main) {
		plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(warning + "You can't use this command.");
			return false;
		}
		boolean override = false;
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("true")) {
				override = true;
			}
		}
		
		Player player = (Player) sender;
		
		List<Faction> allFactions = Faction.allFactions();
		
		Faction rogue = allFactions.get(allFactions.size() - 1);
		
		FactionAccessor factionAccessor = new FactionAccessor(player);
//		Faction playerFaction = Faction.getPlayerFaction(player);
		Faction playerFaction = factionAccessor.getPlayerFaction();
		
		if(playerFaction.equals(rogue)) {
			player.sendMessage(warning + "You are already a " + highlight + rogue.getName() + warning + "!");
			return false;
		}
		
		
		if(!(override)) {
			
			player.sendMessage(warning + "Are you sure that you wish to go "
					+ informative + rogue.getName()
					+ warning + "? If so, append the string " + highlight + "true " + warning + "to the command.");
			return false;
				
			
		}
//		Faction.setPlayerFaction(player, rogue);
		factionAccessor.setPlayerFaction(rogue);
		
		player.sendMessage(good + "You have gone " + regal + rogue.getName() + good + "!");
		
		return true;
	}
}
