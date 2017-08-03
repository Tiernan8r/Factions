package me.Tiernanator.Factions.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Tiernanator.Factions.FactionsMain;
import me.Tiernanator.Factions.Factions.Faction;
import me.Tiernanator.Factions.Factions.FactionAccessor;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.Players.GetPlayer;

public class SetFaction implements CommandExecutor {

	// plugin has changed to static because the function references it.
	@SuppressWarnings("unused")
	private static FactionsMain plugin;

	// Colour Constants
	private static ChatColor highlight = Colour.ALTERNATE_HIGHLIGHT.getColour();
	private static ChatColor warning = Colour.ALTERNATE_WARNING.getColour();
	private static ChatColor good = Colour.ALTERNATE_GOOD.getColour();
	private static ChatColor informative = Colour.ALTERNATE_INFORMATIVE
			.getColour();
	private static ChatColor bad = Colour.ALTERNATE_BAD.getColour();

	public SetFaction(FactionsMain main) {
		plugin = main;
	}

	/*
	 * This Command sets the specified players' faction, only the console, ops
	 * and owners are allowed to use it.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (sender instanceof Player) {
			if (!(sender.hasPermission("factions.set"))) {
				sender.sendMessage(warning + "You can't use this command.");
				return false;
			}
		}

		if (args.length < 2) {
			sender.sendMessage(
					warning + "You must specify a Player and a Faction.");
			return false;
		}

		OfflinePlayer playerForFaction = GetPlayer.getOfflinePlayer(args[0],
				sender, warning, highlight);

		if (playerForFaction == null) {
			return false;
		}

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.equals(playerForFaction)) {
				player.sendMessage(
						warning + "You cannot set your own faction!");
				return false;
			}
		}

		// the group to be added to is the second argument.
		String factionName = args[1];
		List<Faction> allFactions = Faction.allFactions();

		Faction playerFaction = Faction.getFaction(factionName);

		if (playerFaction == null) {
			sender.sendMessage(
					bad + "That is not a faction, the Factions are:");
			// tell them each group in turn
			for (Faction faction : allFactions) {
				sender.sendMessage(
						informative + " - " + faction.getName() + ".");
			}
			return false;
		}

		FactionAccessor factionAccessor = new FactionAccessor(playerForFaction);
		Faction currentFaction = factionAccessor.getPlayerFaction();
		
		if(currentFaction.equals(playerFaction)) {
			sender.sendMessage(bad + "The Player " + highlight + playerForFaction.getName() + bad + " is already in the faction " + highlight + factionName + bad + ".");
			return false;
		}
		// Faction.setPlayerFaction(playerForFaction, playerFaction);
		factionAccessor.setPlayerFaction(playerFaction);

		boolean rogueFaction = playerFaction.getName().equalsIgnoreCase("Rogue");
		
		if (playerForFaction.isOnline()) {

			Player playerReceivedFaction = (Player) playerForFaction;
			
			if (rogueFaction) {
				playerReceivedFaction.sendMessage(good + "You have gone " + highlight
						+ playerFaction.getName() + good + "!");

				return true;
			}

			playerReceivedFaction.sendMessage(good + "You were added to the Faction "
					+ highlight + playerFaction.getName() + good + ".");
		
		}
		
		if (rogueFaction) {
			sender.sendMessage(informative + playerForFaction.getName()
					+ good + " has gone " + highlight
					+ playerFaction.getName() + good + "!");

			return true;
		}
		sender.sendMessage(informative + playerForFaction.getName() + good
				+ " was added to the Faction " + highlight
				+ playerFaction.getName() + good + ".");
		
		return true;
	}

}