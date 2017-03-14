package me.Tiernanator.Factions.Factions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Factions.Main;
import me.Tiernanator.File.ConfigAccessor;

public class Faction {

	private static Main plugin;

	public static void setPlugin(Main main) {
		plugin = main;
	}

	private static List<Faction> allFactions = new ArrayList<Faction>();

	public static List<Faction> allFactions() {
		return allFactions;
	}

	private static void addFaction(Faction faction) {
		if (!factionExists(faction)) {
			allFactions.add(faction);
		}
	}

	public static void removeFaction(Faction faction) {

		if (factionExists(faction)) {
			allFactions.remove(faction);
		}

	}

	public static boolean factionExists(Faction faction) {
		return allFactions().contains(faction);
	}

	public static Faction getFaction(String factionName) {
		for (Faction faction : allFactions()) {
			if (faction.getName().equalsIgnoreCase(factionName)) {
				return faction;
			}
		}
		return null;
	}

	private String name;
	private String prefix;
	private String suffix;
	private boolean canBuild;
	private boolean isDefault;
	private String currency;
	private int subfactions;
	private ChatColor colour;

	public Faction(String name, boolean isDefault, String prefix, String suffix,
			boolean canBuild, String currencyName, int numberSubfactions,
			ChatColor factionChatColour) {

		this.name = name;
		this.isDefault = isDefault;
		this.prefix = prefix;
		this.suffix = suffix;
		this.canBuild = canBuild;
		this.currency = currencyName;
		this.subfactions = numberSubfactions;
		this.colour = factionChatColour;

	}

	public String getName() {
		return this.name;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"factions.yml");
		permissionAccessor.getConfig()
				.set("Factions." + this.getName() + ".prefix", prefix);
		permissionAccessor.saveConfig();
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"factions.yml");
		permissionAccessor.getConfig()
				.set("Factions." + this.getName() + ".suffix", suffix);
		permissionAccessor.saveConfig();
	}

	public ChatColor chatColour() {
		return this.colour;
	}

	public void setColour(ChatColor colour) {

		String colourName = Colour.parseChatColourToCode(colour);

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"factions.yml");
		permissionAccessor.getConfig()
				.set("Factions." + this.getName() + ".colour", colourName);
		permissionAccessor.saveConfig();
	}

	public boolean canBuild() {
		return this.canBuild;
	}

	public void setCanBuild(boolean canBuild) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"factions.yml");
		permissionAccessor.getConfig()
				.set("Factions." + this.getName() + ".build", canBuild);
		permissionAccessor.saveConfig();
	}

	public boolean isDefault() {
		return this.isDefault;
	}

	public void setIsDefault(boolean isDefault) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"factions.yml");
		permissionAccessor.getConfig()
				.set("Factions." + this.getName() + ".default", isDefault);
		permissionAccessor.saveConfig();
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"factions.yml");
		permissionAccessor.getConfig()
				.set("Factions." + this.getName() + ".currency", currency);
		permissionAccessor.saveConfig();
	}

	public int getNumberOfSubfactions() {
		return this.subfactions;
	}

	public void setNumberOfSubfactions(int number) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"factions.yml");
		permissionAccessor.getConfig()
				.set("Factions." + this.getName() + ".subfactions", number);
		permissionAccessor.saveConfig();
	}

	public static List<String> getFactionTypes() {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"factions.yml");
		return permissionAccessor.getConfig().getStringList("Factions.Types");
	}

	public static void initialiseFactions(JavaPlugin plugin) {

		ConfigAccessor factionAccessor = new ConfigAccessor(plugin,
				"factions.yml");
		List<String> allFactionNames = factionAccessor.getConfig()
				.getStringList("Factions.Types");
		for (String factionName : allFactionNames) {

			String name = factionName;
			String prefix = factionAccessor.getConfig()
					.getString("Factions." + factionName + ".prefix");
			String suffix = factionAccessor.getConfig()
					.getString("Factions." + factionName + ".suffix");
			boolean isDefault = factionAccessor.getConfig()
					.getBoolean("Factions." + factionName + ".default");
			String currencyName = factionAccessor.getConfig()
					.getString("Factions." + factionName + ".currency");
			boolean canBuild = factionAccessor.getConfig()
					.getBoolean("Factions." + factionName + ".build");
			int numberSubfactions = factionAccessor.getConfig()
					.getInt("Factions." + factionName + ".subfactions");
			String factionColourCode = factionAccessor.getConfig()
					.getString("Factions." + factionName + ".colour");
			// ChatColor factionColour = Colour.parseCodeToChatColour("&" +
			// factionColourCode);
			ChatColor factionColour = Colour
					.parseCodeToChatColour(factionColourCode);

			Faction faction = new Faction(name, isDefault, prefix, suffix,
					canBuild, currencyName, numberSubfactions, factionColour);
			addFaction(faction);

		}
	}

	public List<String> getFactionMembers() {

//		List<Faction> allFactions = Faction.allFactions();
//		if (!(allFactions.contains(faction))) {
//			return null;
//		}
		List<String> allMembers = new ArrayList<String>();

		OfflinePlayer[] allPlayers = Bukkit.getServer().getOfflinePlayers();
		for (OfflinePlayer player : allPlayers) {
			FactionAccessor factionAccessor = new FactionAccessor(player);
//			Faction playerFaction = getPlayerFaction(player);
			Faction playerFaction = factionAccessor.getPlayerFaction();
			if (playerFaction.equals(this)) {
				allMembers.add(player.getUniqueId().toString());
			}
		}

		return allMembers;

	}

}
