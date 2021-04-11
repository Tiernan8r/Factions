package me.Tiernanator.Factions.FactionEvents;

import me.Tiernanator.Factions.Commands.GetFaction;
import me.Tiernanator.Factions.Factions.Faction;
import me.Tiernanator.Factions.Factions.FactionAccessor;
import me.Tiernanator.Factions.FactionsMain;
import me.Tiernanator.Utilities.Colours.Colour;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FactionEventHandler implements Listener {

	@SuppressWarnings("unused")
	private static FactionsMain plugin;

	ChatColor good = Colour.ALTERNATE_GOOD.getColour();
	ChatColor highlight = Colour.ALTERNATE_HIGHLIGHT.getColour();
	ChatColor warning = Colour.ALTERNATE_WARNING.getColour();
	
	public FactionEventHandler(FactionsMain main) {
		plugin = main;
	}

	public static void setPlugin(FactionsMain main) {
		plugin = main;
	}
	
	@EventHandler
	public void applyFactionOnPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();

		Faction faction = null;
		FactionAccessor factionAccessor = new FactionAccessor(player);
		if(!player.hasPlayedBefore() || !factionAccessor.hasPlayerFaction()) {
//		if(!player.hasPlayedBefore()) {
			
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

}