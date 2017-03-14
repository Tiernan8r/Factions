package me.Tiernanator.Factions.FactionEvents.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.Tiernanator.Factions.Factions.Faction;

//This is the custom player change faction event called when a player changes faction, but not power...

public final class CustomFactionChangeEvent extends Event {
	
	//handlers is a variable "handled"(...) by the server
    private static final HandlerList handlers = new HandlerList();
    //the player who got a new group
    private Player player;
    //The new faction
    private Faction faction;
    

    //constructor for the event that sets the variables
    public CustomFactionChangeEvent(Player player, Faction newFaction) {
        this.player = player;
        this.faction = newFaction;
    }

    //get the player who done it
    public Player getPlayer() {
        return player;
    }
    //get the new faction
    public Faction getFaction() {
    	return this.faction;
    }

    
    //the next two are necessary for the server to use the event
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}