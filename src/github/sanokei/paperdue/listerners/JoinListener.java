package github.sanokei.paperdue.listerners;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.gson.Gson;

import github.sanokei.paperdue.Main;
import github.sanokei.paperdue.files.PlayerCustom;

public class JoinListener {
	@SuppressWarnings("unused")
	private Main plugin;
	public JoinListener(Main plugin){
		setPlugin(plugin);
	}
	
	private void setPlugin(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void OnJoin(PlayerJoinEvent event){ 
		if(!event.getPlayer().hasPlayedBefore()){
			Bukkit.broadcastMessage(
				ChatColor.translateAlternateColorCodes('&', ("&a<player> &7has joined the fight for the first time...").replace("<player>", event.getPlayer().getName()))
			);
			//NPCManager.sendPlayerNpcPackets(event.getPlayer());
			//TODO Add Merchants ^
			
			//Adds new player to Player JSON
			newPlayerToJson(event.getPlayer());
		}
		else{
			Bukkit.broadcastMessage(
				//TODO Add feature to show players in your faction who joined
				ChatColor.translateAlternateColorCodes('&', ("&a<player> &7has come back into the fight...").replace("<player>", event.getPlayer().getName())) //could make it getDisplayName();
			);
		}
	}

	private void newPlayerToJson(Player player) {
		File playerFile = new File(player.getUniqueId().toString()+".json");
		try {
			if(playerFile.createNewFile()) {
				try {
					Writer writer = Files.newBufferedWriter(Paths.get(player.getUniqueId().toString()+".json"));
					Gson gson = new Gson();
					gson.toJson(new PlayerCustom(), writer);
					writer.close();
						
				}
				catch(Exception e) {
					player.sendMessage("Something went very wrong.");
				}
			}
			else {
				player.sendMessage("Something went very wrong: Not new player.");
			}
		}
		catch (IOException e) {
			player.sendMessage("Something went very wrong: Name not compliant.");
		}
	}
}
