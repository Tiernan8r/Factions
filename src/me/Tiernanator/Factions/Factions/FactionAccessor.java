package me.Tiernanator.Factions.Factions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Tiernanator.Factions.Main;
import me.Tiernanator.Factions.FactionEvents.CustomEvents.CustomFactionChangeEvent;

public class FactionAccessor {

	private static Main plugin;
	public static void setPlugin(Main main) {
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
		Connection connection = Main.getSQL().getConnection();
		PreparedStatement preparedStatement= null;
		try {
			preparedStatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		ResultSet resultSet = null;
		try {
//			resultSet = statement.executeQuery(query);
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (!resultSet.isBeforeFirst()) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String factionName = null;
		try {
			factionName = resultSet.getString("Faction");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return factionName;
		
//		String query = "SELECT Faction FROM FactionUsers WHERE UUID = '"
//				+ playerUUID + "';";
//
//		Connection connection = Main.getSQL().getConnection();
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
//
//		try {
//			if (!resultSet.isBeforeFirst()) {
//				return null;
//			}
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		try {
//			resultSet.next();
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//
//		String factionName = null;
//		try {
//			factionName = resultSet.getString("Faction");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return factionName;
		
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

					Connection connection = Main.getSQL().getConnection();
					PreparedStatement preparedStatement = null;
					try {
						preparedStatement = connection.prepareStatement(
								"INSERT INTO FactionUsers (UUID, Faction) VALUES (?, ?);");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						preparedStatement.setString(1, playerUUID);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						preparedStatement.setString(2, faction.getName());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						preparedStatement.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {

					String query = "UPDATE FactionUsers SET Faction = '"
							+ faction.getName() + "' WHERE UUID = '"
							+ playerUUID + "';";

					Connection connection = Main.getSQL().getConnection();
					Statement statement = null;
					try {
						statement = connection.createStatement();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						statement.execute(query);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				
				if(getPlayer().isOnline()) {
					
					Player player = (Player) getPlayer();
					
					CustomFactionChangeEvent factionChangeEvent = new CustomFactionChangeEvent(
							player, faction);
					plugin.getServer().getPluginManager().callEvent(factionChangeEvent);
					
				}

			}
		};
		runnable.runTaskAsynchronously(plugin);
		
//		if (!hasPlayerFaction(player)) {
//
//			Connection connection = Main.getSQLConnection();
//			PreparedStatement preparedStatement = null;
//			try {
//				preparedStatement = connection.prepareStatement(
//						"INSERT INTO FactionUsers (UUID, Faction) VALUES (?, ?);");
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			try {
//				preparedStatement.setString(1, playerUUID);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			try {
//				preparedStatement.setString(2, faction.getName());
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			try {
//				preparedStatement.executeUpdate();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		} else {
//
//			String query = "UPDATE FactionUsers SET Faction = '"
//					+ faction.getName() + "' WHERE UUID = '"
//					+ playerUUID + "';";
//
//			Connection connection = Main.getSQLConnection();
//			Statement statement = null;
//			try {
//				statement = connection.createStatement();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			try {
//				statement.execute(query);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//
//		CustomFactionChangeEvent factionChangeEvent = new CustomFactionChangeEvent(
//				player, faction);
//		plugin.getServer().getPluginManager().callEvent(factionChangeEvent);
		
	}
	

	public void setPlayerFaction(String factionName) {
		
		Faction faction = Faction.getFaction(factionName);
		if (faction == null) {
			return;
		}
		setPlayerFaction(faction);
		
	}

	private boolean hasPlayerFaction = false;
	public boolean hasPlayerFaction() {
		
			String playerUUID = player.getUniqueId().toString();

		BukkitRunnable runnable = new BukkitRunnable() {

			@Override
			public void run() {

				String query = "SELECT * FROM FactionUsers WHERE UUID = '" + playerUUID + "';";

				Connection connection = Main.getSQL().getConnection();
				Statement statement = null;
				try {
					statement = connection.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ResultSet resultSet = null;
				try {
					resultSet = statement.executeQuery(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (!resultSet.isBeforeFirst()) {
						FactionAccessor.this.hasPlayerFaction = false;
						return;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					resultSet.next();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				String result = null;
				try {
					result = resultSet.getString("Faction");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				boolean hasResult = !(result == null);
				FactionAccessor.this.hasPlayerFaction = hasResult;
				
			}
		};
		runnable.runTaskAsynchronously(plugin);
		return FactionAccessor.this.hasPlayerFaction;
		
//		String query = "SELECT * FROM FactionUsers WHERE UUID = '" + playerUUID + "';";
//
//		Connection connection = Main.getSQL().getConnection();
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
//		String hasResult = null;
//		try {
//			hasResult = resultSet.getString("Faction");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		if(hasResult == null) {
//			return false;
//		} else {
//			return true;
//		}
		
	}

}
