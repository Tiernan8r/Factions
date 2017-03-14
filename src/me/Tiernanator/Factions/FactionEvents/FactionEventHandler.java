package me.Tiernanator.Factions.FactionEvents;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Factions.Main;
import me.Tiernanator.Factions.Commands.GetFaction;
import me.Tiernanator.Factions.Factions.Faction;
import me.Tiernanator.Factions.Factions.FactionAccessor;

public class FactionEventHandler implements Listener {

	@SuppressWarnings("unused")
	private static Main plugin;

	ChatColor good = Colour.ALTERNATE_GOOD.getColour();
	ChatColor highlight = Colour.ALTERNATE_HIGHLIGHT.getColour();
	ChatColor warning = Colour.ALTERNATE_WARNING.getColour();
	
	public FactionEventHandler(Main main) {
		plugin = main;
	}

	public static void setPlugin(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void applyFactionOnPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();

		Faction faction = null;
		FactionAccessor factionAccessor = new FactionAccessor(player);
//		if(!player.hasPlayedBefore() || !hasValue(player)) {
		if(!player.hasPlayedBefore()) {
			for(Faction f : Faction.allFactions()) {
				if(f.isDefault()) {
					faction = f;
				}
			}
			factionAccessor.setPlayerFaction(faction);
			player.sendMessage(good + "As it is your first time on the server, you have been added to the default faction: " + highlight
					+ faction.getName());
		} else {
			GetFaction.playerInformFaction(player);
		}
	}

//	@SuppressWarnings("unused")
//	private boolean hasValue(Player player) {
//		
//		String query = "SELECT Faction FROM FactionUsers WHERE " + "UUID = '" + player.getUniqueId().toString() + "';";
//
//		Connection connection = Main.getSQLConnection();
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		ResultSet resultSet = null;
//		try {
//			resultSet = statement.executeQuery(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			if (!resultSet.isBeforeFirst()) {
//				return false;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			resultSet.next();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		String playerFactionName = Faction.getPlayerFactionName(player);
//		String resultName = null;
//
//		try {
//			resultName = resultSet.getString("Faction");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return playerFactionName.equalsIgnoreCase(resultName);
//		
//	}
	
}
