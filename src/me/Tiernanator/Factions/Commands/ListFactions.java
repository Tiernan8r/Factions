package me.Tiernanator.Factions.Commands;

import me.Tiernanator.Factions.Factions.Faction;
import me.Tiernanator.Factions.FactionsMain;
import me.Tiernanator.Utilities.Colours.Colour;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListFactions implements CommandExecutor {

	@SuppressWarnings("unused")
	private FactionsMain plugin;

	public ListFactions(FactionsMain main) {
		plugin = main;
	}
	
	private static ChatColor good = Colour.ALTERNATE_GOOD.getColour();
	private static ChatColor informative = Colour.ALTERNATE_INFORMATIVE.getColour();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		listFactions(sender);
		return true;
	}

	public static void listFactions(CommandSender sender) {
		
		List<Faction> factions = Faction.allFactions();
		sender.sendMessage(good + "The Factions are:");
		for (Faction faction : factions) {
			sender.sendMessage(informative + " - " + faction.getName() + ".");
		}
	}

}
