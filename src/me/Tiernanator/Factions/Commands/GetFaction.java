package me.Tiernanator.Factions.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Factions.FactionsMain;
import me.Tiernanator.Factions.Factions.Faction;
import me.Tiernanator.Factions.Factions.FactionAccessor;
import me.Tiernanator.Utilities.Players.GetPlayer;

public class GetFaction implements CommandExecutor {

	@SuppressWarnings("unused")
	private static FactionsMain plugin;

	// I told you they recurred...(I did in TestPermission anyway)
	private ChatColor informative = Colour.INFORMATIVE.getColour();
	private ChatColor warning = Colour.ALTERNATE_WARNING.getColour();
	private static ChatColor highlight = Colour.ALTERNATE_HIGHLIGHT.getColour();
	private static ChatColor good = Colour.ALTERNATE_GOOD.getColour();
	private ChatColor bad = Colour.ALTERNATE_BAD.getColour();

	// this has to stay the Main class won't be happy.
	public GetFaction(FactionsMain main) {
		plugin = main;
	}
	
	// this Command Sends the player a message with their Power display name.
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if(args.length > 1) {
			Player player = GetPlayer.getPlayer(args[0], sender, warning, highlight);
			if(player == null) {
				return false;
			}
//			Faction faction = Faction.getPlayerFaction(player);
			FactionAccessor factionAccessor = new FactionAccessor(player);
			Faction faction = factionAccessor.getPlayerFaction();
			sender.sendMessage(good + "The player " + informative + player.getName() + good + " is a member of the Faction: "
					+ highlight + faction.getName());
			return true;
		}
		
		// only Players have permissions...
		if (!(sender instanceof Player)) {
			sender.sendMessage(warning + "You must specify a player name.");
			return false;
		}
		// get the player
		Player player = (Player) sender;
		
		// uses the getPlayerFaction() function found in Faction.java
//		Faction faction = Faction.getPlayerFaction(player);
		FactionAccessor factionAccessor = new FactionAccessor(player);
		Faction faction = factionAccessor.getPlayerFaction();
		
		// if they are in a valid group, tell them the group
		if (faction == null) {

			player.sendMessage(bad
					+ "I have absolutely no clue what Faction you belong to!!");
			return false;

		} else {
			if(faction.getName().equalsIgnoreCase("Rogue")) {
				player.sendMessage(good + "You are a "
						+ highlight + faction.getName() + good + "!");
				return true;
			}

			player.sendMessage(good + "You are a member of the Faction: "
					+ highlight + faction.getName());

		}
		return true;

	}

	public static void playerInformFaction(Player player) {
		FactionAccessor factionAccessor = new FactionAccessor(player);
		Faction playerFaction = factionAccessor.getPlayerFaction();
		String playerFactionName = playerFaction.getName();
//		String playerFaction = Faction.getPlayerFaction(player).getName();
		player.sendMessage(good + "You are a member of the Faction: "
				+ highlight + playerFactionName);
		
	}

}
