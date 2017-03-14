package me.Tiernanator.Factions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.Factions.Commands.GetFaction;
import me.Tiernanator.Factions.Commands.ListFactions;
import me.Tiernanator.Factions.Commands.Rogue;
import me.Tiernanator.Factions.Commands.SetFaction;
import me.Tiernanator.Factions.FactionEvents.FactionEventHandler;
import me.Tiernanator.Factions.Factions.Faction;
import me.Tiernanator.Factions.Factions.FactionAccessor;
import me.Tiernanator.SQL.SQLServer;
import me.Tiernanator.SQL.MySQL.MySQL;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		FactionAccessor.setPlugin(this);
		
		allocateConstants();
		initialiseSQL();
		
		registerCommands();
		registerEvents();
		
	}

	@Override
	public void onDisable() {
			
		try {
			getSQL().closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void registerCommands() {
		// all the commands
		getCommand("setFaction").setExecutor(new SetFaction(this));
		getCommand("getFaction").setExecutor(new GetFaction(this));
		getCommand("listFactions").setExecutor(new ListFactions(this));
		getCommand("rogue").setExecutor(new Rogue(this));
		
	}

	public void registerEvents() {
		// the two events handle the player leaving & joining
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new FactionEventHandler(this), this);
		
	}

	
	private void allocateConstants() {
		Faction.setPlugin(this);
		Faction.initialiseFactions(this);
		FactionEventHandler.setPlugin(this);
	}
	
	private static MySQL mySQL;

	private void initialiseSQL() {
		
		mySQL = new MySQL(SQLServer.HOSTNAME, SQLServer.PORT, SQLServer.DATABASE,
				SQLServer.USERNAME, SQLServer.PASSWORD);
		
//		String query = "CREATE DATABASE IF NOT EXISTS factions;";
		
		Connection connection = null;
		try {
			connection = mySQL.openConnection();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
//		try {
//			statement.execute(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		query = "USE factions;";
		String query = "USE " + SQLServer.DATABASE.getInfo() +  ";";
		
		statement = null;
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
		
		query = "CREATE TABLE IF NOT EXISTS FactionUsers ( "
				+ "UUID varchar(36), "
				+ "Faction varchar(255)"
				+ ");";
		
		statement = null;
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
		
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static MySQL getSQL() {
		return mySQL;
	}

//	public static Connection getSQLConnection() {
//
//		try {
//			if (!getSQL().checkConnection()) {
//			return getSQL().openConnection();
//		} else {
//			return getSQL().getConnection();
//		}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//		Connection connection = null;
//		try {
//			if (!getSQL().checkConnection()) {
//				connection = getSQL().openConnection();
//			} else {
//				connection = getSQL().getConnection();
//			}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		String query = "USE " + SQLServer.DATABASE.getInfo() + ";";
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			statement.execute(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return connection;
//	}
}
