package me.Tiernanator.Factions.Factions;

import me.Tiernanator.Factions.FactionEvents.CustomEvents.CustomFactionChangeEvent;
import me.Tiernanator.Factions.FactionsMain;
import me.Tiernanator.Utilities.SQL.SQLServer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class FactionAccessor {

	private static FactionsMain plugin;
	public static void setPlugin(FactionsMain main) {
		plugin = main;
	}

	private OfflinePlayer player;
	public FactionAccessor(OfflinePlayer player) {
		this.player = player;
	}

	public OfflinePlayer getPlayer() {
		return this.player;
	}

	public String getPlayerFactionName() {

		String playerUUID = getPlayer().getUniqueId().toString();
		String query = "SELECT Faction FROM FactionUsers WHERE UUID = '"
				+ playerUUID + "';";
		return SQLServer.getString(query, "Faction");

	}

	public Faction getPlayerFaction() {

		String factionName = getPlayerFactionName();
		Faction faction = Faction.getFaction(factionName);
		return faction;

	}

	public void setPlayerFaction(Faction faction) {

		String playerUUID = getPlayer().getUniqueId().toString();

		if (!hasPlayerFaction()) {

			String statement = "INSERT INTO FactionUsers (UUID, Faction) VALUES (?, ?);";
			Object[] values = new Object[]{playerUUID, faction.getName()};
			SQLServer.executePreparedStatement(statement, values);

		} else {

			String statement = "UPDATE FactionUsers SET Faction = ? WHERE UUID = ?;";
			Object[] values = new Object[] {faction.getName(), playerUUID};
			SQLServer.executePreparedStatement(statement, values);
			
		}

		if (getPlayer().isOnline()) {

			Player player = (Player) getPlayer();

			CustomFactionChangeEvent factionChangeEvent = new CustomFactionChangeEvent(
					player, faction);
			plugin.getServer().getPluginManager().callEvent(factionChangeEvent);

		}

	}

	public void setPlayerFaction(String factionName) {

		Faction faction = Faction.getFaction(factionName);
		if (faction == null) {
			return;
		}
		setPlayerFaction(faction);

	}

	public boolean hasPlayerFaction() {

		String playerUUID = getPlayer().getUniqueId().toString();

		String query = "SELECT * FROM FactionUsers WHERE UUID = '" + playerUUID
				+ "';";

		String faction = SQLServer.getString(query, "Faction");
		return faction != null;

	}
}
