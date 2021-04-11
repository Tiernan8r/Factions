package me.Tiernanator.Factions;

import me.Tiernanator.Factions.Commands.GetFaction;
import me.Tiernanator.Factions.Commands.ListFactions;
import me.Tiernanator.Factions.Commands.Rogue;
import me.Tiernanator.Factions.Commands.SetFaction;
import me.Tiernanator.Factions.FactionEvents.FactionEventHandler;
import me.Tiernanator.Factions.Factions.Faction;
import me.Tiernanator.Factions.Factions.FactionAccessor;
import me.Tiernanator.Utilities.SQL.SQLServer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FactionsMain extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		FactionAccessor.setPlugin(this);
		
		allocateConstants();
		initialiseSQL();
		
		registerCommands();
		registerEvents();
		
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
	
	private void initialiseSQL() {
		
		String query = "CREATE TABLE IF NOT EXISTS FactionUsers ( "
				+ "UUID varchar(36) NOT NULL, "
				+ "Faction varchar(255) NOT NULL,"
				+ "PRIMARY KEY (UUID)"
				+ ");";
		SQLServer.executeQuery(query);
		
	}

}
