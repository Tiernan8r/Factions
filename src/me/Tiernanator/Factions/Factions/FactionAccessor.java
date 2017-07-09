package me.Tiernanator.Factions.Factions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Tiernanator.Factions.FactionsMain;
import me.Tiernanator.Factions.FactionEvents.CustomEvents.CustomFactionChangeEvent;
import me.Tiernanator.SQL.SQLServer;

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
		BukkitRunnable runnable = new BukkitRunnable() {

			@Override
			public void run() {

				if (!hasPlayerFaction()) {

					String statement = "INSERT INTO FactionUsers (UUID, Faction) VALUES (?, ?);";
					Object[] values = new Object[]{playerUUID,
							faction.getName()};
					SQLServer.executePreparedStatement(statement, values);
				} else {

					String query = "UPDATE FactionUsers SET Faction = '"
							+ faction.getName() + "' WHERE UUID = '"
							+ playerUUID + "';";

					SQLServer.executeQuery(query);
				}

				if (getPlayer().isOnline()) {

					Player player = (Player) getPlayer();

					CustomFactionChangeEvent factionChangeEvent = new CustomFactionChangeEvent(
							player, faction);
					plugin.getServer().getPluginManager()
							.callEvent(factionChangeEvent);

				}

			}
		};
		runnable.runTaskAsynchronously(plugin);

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
