package net.paperdue.factions.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.paperdue.factions.Main;
import net.paperdue.factions.files.Faction;
import net.paperdue.factions.files.PlayerCustom;

public class FactionsCommand implements CommandExecutor{
	public static String commandName = "Clan";
	public final String TOO_MANY_ARGUMENTS = ChatColor.RED + ""+ ChatColor.BOLD +"Command "+commandName+" had too many arguments";
	public final String DOES_NOT_EXIST = ChatColor.RED + ""+ ChatColor.BOLD +"Command "+commandName+" Does not exist. Please run /help for more information on this command.";
	public final String NO_PERMISSION = ChatColor.RED + ""+ ChatColor.BOLD +"You do not have permission to execute the "+commandName+" command.";
	public final String ONLY_PLAYERS_ALLOWED_TO_EXECUTE = ChatColor.RED + ""+ ChatColor.BOLD + "Only players may execute the "+commandName+" command. Hi console!";
	public final String NOT_ENOUGH_ARGUMENTS = ChatColor.RED + ""+ ChatColor.BOLD +"Command "+commandName+" had too few arguments. Try /help for more information on the command.";
	@SuppressWarnings("unused")
	private Main plugin;
	
	public FactionsCommand(Main main){
		this.setPlugin(main);
	}
	
	private void setPlugin(Main plugin) {
		this.plugin=plugin;
	}
	public static UUID getUUID(String name) {
	    String uuid = "";
	    BufferedReader in;
	    try {
	        in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
	        uuid = (((JsonObject)new JsonParser().parse(in)).get("id")).toString().replaceAll("\"", "");
	        uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
	        in.close();
	    } catch (Exception e) {
	        System.out.println("Unable to get UUID of: " + name + "!");
	        uuid = "er";
	        return null;
	    }
	    return UUID.fromString(uuid);
	}
	public static void messageMembers(PlayerCustom playerCustom, Player player, String message) {
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		for(UUID members : faction.members.keySet()) {
			Player p = Bukkit.getPlayer(members);
			try {
				p.sendMessage(message);
			} catch(Exception e) {}
		}
	}
	public static boolean writeFile(Object write, String name, Player player) {
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(name+".json"));
			Gson gson = new Gson();
			gson.toJson(write, writer);
			writer.close();
			return true;
		}
		catch(Exception e) {
			player.sendMessage("Error: Name existent, or fatal partician error. Its a coin flip really");
			return false;
		}
	}
	public static Object readFacFile(String name, Player player){
		try {
			Gson gson = new Gson();
			Reader reader = Files.newBufferedReader(Paths.get(name+".json"));
			Faction playJ = gson.fromJson(reader,Faction.class);
			reader.close();
			if(playJ == null) {
				player.sendMessage("Error: "+commandName+" remained null.");
				return false;
			}
			return playJ;
			
		}
		catch(Exception e) {
			player.sendMessage("Error: Nonexistent, TypeError or Typo.");
			return false;
		}
	}
	public static Object readPCFile(String name, Player player){
		try {
			Gson gson = new Gson();
			Reader reader = Files.newBufferedReader(Paths.get(name+".json"));
			PlayerCustom playJ = gson.fromJson(reader,PlayerCustom.class);
			reader.close();
			if(playJ == null) {
				player.sendMessage("Error: Player remained null.");
				return false;
			}
			return playJ;
			
		}
		catch(Exception e) {
			player.sendMessage("Error: Player not online or does not exist");
			return false;
		}
	}
	
	//Commands
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String permission, String[] args){
		//Catch all Errors
		if(sender instanceof Player){
			sender.sendMessage(ONLY_PLAYERS_ALLOWED_TO_EXECUTE);
			return false;
		}
		
		Player player = (Player)sender;
		
		//check args length for individual commands inside of the functions
		
		if(args.length == 0){
			//TODO Show the faction menu instead
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		/* Create player readers */
		PlayerCustom playerCustom = (PlayerCustom) readPCFile(player.getUniqueId().toString(),player);
		
		//check type of command
		switch(args[0].toLowerCase()){
			case "create":
				//creates faction for player
				return createFaction(playerCustom, args, player);
			case "join":
				return joinFaction(playerCustom, args, player);
			case "leave":
				return leaveFaction(playerCustom, args, player);
			case "invite":
				return inviteFaction(playerCustom,args,player);
			case "ban":
				return banFaction(playerCustom,args,player);
			case "disband":
				return disbandFaction(playerCustom,args,player);
			case "promote":
				return promoteFaction(playerCustom,args,player);
			case "demote":
				return demoteFaction(playerCustom,args,player);
			case "open":
				return openFaction(playerCustom,args,player);
			case "close":
				return closeFaction(playerCustom,args,player);
			case "ally":
				return allyFaction(playerCustom,args,player);
			case "enemy":
				return enemyFaction(playerCustom,args,player);
			case "neutral":
				return neutralFaction(playerCustom,args,player);
			case "who":
				return whoFaction(playerCustom,args,player);
			case "name":
				return nameFaction(playerCustom,args,player);
			case "desc":
				return descFaction(playerCustom,args,player);  
			case "banner":
				return bannerFaction(playerCustom,args,player);
			case "help":
				player.sendMessage("/help");
				return false;
			default:
				player.sendMessage(DOES_NOT_EXIST);
				return false;
		}
	}

	private boolean createFaction(PlayerCustom playerCustom,String[] args, Player player) {
		//closed by default
		//f create name description
		//f   0     1       2      
		
		//------------- Faction creation compliance ---------------//
		if(playerCustom.infac) {
			player.sendMessage("You are in a "+commandName+" already");
			return false;
		}
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 3) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		//------------------ Initialize faction ------------------//
		File facFile = new File(args[1].toLowerCase()+".json");
		try {
			if(!facFile.createNewFile()) {
				player.sendMessage("This "+commandName+" already exists");
				return false;
			}
		}
		catch (IOException e) {
			player.sendMessage("This is not a compliant name.");
			return false;
		}
		//
		writeFile(new Faction(args,player),args[1],player);
		//--------------------- Change Player JSON ----------------------//
		PlayerCustom newPC = playerCustom;
		newPC.infac = true;
		newPC.fac_Name = args[1];
		newPC.fac_role = "owner";
		writeFile(newPC,player.getUniqueId().toString(),player);
		player.sendMessage(commandName+" "+args[1]+" has been created.");
		return true;
	}
	
	private boolean joinFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f join name 
		//f   0     1
		
		//------------- Faction compliance ---------------//
		if(playerCustom.infac) {
			player.sendMessage("You are in a "+commandName+" already");
			return false;
		}
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		//------------------ Initialize faction ------------------//
		File facFile = new File(args[1].toLowerCase()+".json");
		try {
			if(facFile.createNewFile()) {
				facFile.delete();
				player.sendMessage(commandName+" does not exist");
				return false;
			}
		}
		catch(Exception e) {
			player.sendMessage("Name compliance error");
			return false;
		}
		Faction faction = (Faction) readFacFile(args[1].toLowerCase(),player);

		if(faction.banned.contains(player.getUniqueId())) {
			player.sendMessage("You have been banned from this "+commandName);
			return false;
		}
		//--------------------- Change Faction and Player ----------------------//
		if(faction.type == "open"){
			Faction newfac = faction;
			try {newfac.invited.remove(player.getUniqueId());}
			catch(Exception e) {}
			newfac.peasants.add(player.getUniqueId());
			newfac.members.put(player.getUniqueId(),player.getDisplayName());
			writeFile(newfac, args[1].toLowerCase(), player);
			
			PlayerCustom newPC = playerCustom;
			newPC.infac = true;
			newPC.fac_Name = args[1];
			newPC.fac_role = "peasant";
			writeFile(newPC, player.getUniqueId().toString(), player);

			player.sendMessage("You have joined"+args[1]);
			messageMembers(playerCustom, player, "player "+player.getName()+" has joined "+playerCustom.fac_Name);
			return true;
		}
		else{
			if(faction.invited.contains(player.getUniqueId())) {
				
				Faction newfac = faction; 
				newfac.invited.remove(player.getUniqueId());
				newfac.peasants.add(player.getUniqueId());
				newfac.members.put(player.getUniqueId(),player.getDisplayName());
				writeFile(newfac, args[1].toLowerCase(), player);
				
				PlayerCustom newPC = playerCustom;
				newPC.infac = true;
				newPC.fac_Name = args[1];
				newPC.fac_role = "peasant";
				writeFile(newPC, player.getUniqueId().toString(), player);
				player.sendMessage("You have joined"+args[1]);
				messageMembers(playerCustom, player, "player "+player.getName()+" has joined "+playerCustom.fac_Name);
				return true;
			}
			else {
				player.sendMessage("You were not invited to this "+commandName);
				return false;
			}
		}
	}

	private boolean leaveFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f leave
		//f   0  
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}
			
		if(args.length < 1) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 1) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		//------------- Faction compliance ---------------//
		if(playerCustom.fac_role == "owner" && faction.owner.size() < 2) {
			player.sendMessage("You cannot leave your own "+commandName+"unless there is another owner. Try /"+commandName+" disband.");
			return false;
		}
		if(playerCustom.fac_role == "owner" && faction.owner.size() > 1) {
			player.sendMessage("You cannot leave your own "+commandName+" until you demote yourself as owner. Try /"+commandName+" demote "+player.getName());
			return false;
		}
		//--------------------- Change Faction and Player ----------------------//
		if(playerCustom.fac_role == "peasant")
			faction.peasants.remove(player.getUniqueId());

		if(playerCustom.fac_role == "officer")
			faction.officer.remove(player.getUniqueId());
		
		if(playerCustom.fac_role == "moderator")
			faction.moderator.remove(player.getUniqueId());
		
		faction.members.remove(player.getUniqueId());
		writeFile(faction, playerCustom.fac_Name.toLowerCase(), player);
		
		PlayerCustom newPC = playerCustom;
		newPC.infac = false;
		newPC.fac_Name = null;
		newPC.fac_role = null;
		writeFile(newPC, player.getUniqueId().toString(), player);
		
		player.sendMessage("You have left"+args[1]);
		messageMembers(playerCustom, player, "player "+player.getName()+" has left "+playerCustom.fac_Name);
		return true;
	}
	
	private boolean inviteFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f invite name
		//f   0      1 	
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}

		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator" || playerCustom.fac_role != "officer") {
			player.sendMessage("Only members higher than officer can invite new "+commandName+" members");
			return false;
		}
			
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		Player playerbyname = null;
		try {
			//Yeah its deprecated but it will work for now.
			playerbyname = (Player) Bukkit.getOfflinePlayer(getUUID(args[1]));
		}
		catch(Exception e){
			player.sendMessage("Player does not exist");
			return false;
		}
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		//--------------------- Change Faction and Player ----------------------//
		for(UUID uuid : faction.invited)
			if(playerbyname.getUniqueId().equals(uuid)) {
				player.sendMessage("This player is already invited");
				return true;
			}
		faction.invited.add(playerbyname.getUniqueId());
		writeFile(faction, playerCustom.fac_Name.toLowerCase(), player);
		try {
			playerbyname.sendMessage("You have been invited to "+playerCustom.fac_Name+". By player "+player.getName());
		}catch(Exception e) {}
		player.sendMessage("Player was invited");
		return true;
	}

	private boolean banFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f ban name
		//f   0      1 	
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}

		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can invite new "+commandName+" members");
			return false;
		}
			
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		Player playerbyname = null;
		try {
			//Yeah its deprecated but it will work for now.
			playerbyname = (Player) Bukkit.getOfflinePlayer(getUUID(args[1]));
		}
		catch(Exception e){
			player.sendMessage("Player does not exist");
			return false;
		}
		if(playerbyname == null) {
			player.sendMessage("Player does not exist");
			return false;
		}
		
		//------------------ Initialize faction ------------------//
		Faction newfac = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		//------------------ Initialize player ------------------//
		PlayerCustom newpc = (PlayerCustom) readPCFile(playerbyname.getUniqueId().toString(),player);
		//------------- Faction compliance ---------------//
		if(newpc.fac_Name != playerCustom.fac_Name) {
			player.sendMessage("You cannot ban someone not in your "+commandName);
			return false;
		}
		if(newpc.fac_role == "owner") {
			player.sendMessage("You cannot ban the owner of your "+commandName);
			return false;
		}
		if(newpc.fac_role == "moderator" && playerCustom.fac_role == "moderator") {
			player.sendMessage("You cannot ban a fellow moderator");
			return false;
		}
		//--------------------- Change Faction and Player ----------------------//
		try {
			newfac.invited.remove(playerbyname.getUniqueId());
		}
		catch(Exception e) {
		}
		if(newpc.fac_role == "peasant")
			newfac.peasants.remove(playerbyname.getUniqueId());

		if(newpc.fac_role == "officer")
			newfac.officer.remove(playerbyname.getUniqueId());
		
		if(newpc.fac_role == "moderator")
			newfac.moderator.remove(playerbyname.getUniqueId());
		
		newpc.infac = false;
		newpc.fac_Name = null;
		newpc.fac_role = null;
		writeFile(newpc, playerbyname.getUniqueId().toString(), player);
		
		newfac.members.remove(player.getUniqueId());
		newfac.banned.add(playerbyname.getUniqueId());
		writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
		messageMembers(playerCustom, player, "player "+args[1]+" has been banned from "+playerCustom.fac_Name+". By player "+player.getName());
		try {
			playerbyname.sendMessage("You have been banned from "+playerCustom.fac_Name+". By player "+player.getName());
		}
		catch(Exception e) {
			
		}
		return true;
	}

	private boolean disbandFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f disband
		//f   0       
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}
		if(playerCustom.fac_role != "owner") {
			player.sendMessage("Only the owner can disband a "+commandName);
			return false;
		}
		if(args.length < 1) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 1) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		File facFile = new File(playerCustom.fac_Name.toLowerCase()+".json");
		PlayerCustom newpc;
		//--------------------- Change Faction and Player ----------------------//
		messageMembers(playerCustom, player, playerCustom.fac_Name+" is being disbanded. By player "+player.getName());
		for(UUID members : faction.members.keySet()) {
			newpc = (PlayerCustom) readPCFile(members.toString(),player);
			newpc.infac = false;
			newpc.fac_Name = null;
			newpc.fac_role = null;
			writeFile(newpc, members.toString(), player);
		}
		facFile.delete();
		player.sendMessage(commandName+" has been deleted");
		return true;
	}
	
	private boolean promoteFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f promote name
		//f   0      1 	
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}

		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can promote "+commandName+" members");
			return false;
		}
			
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		Player playerbyname = null;
		try {
			playerbyname = (Player) Bukkit.getOfflinePlayer(getUUID(args[1]));
		}
		catch(Exception e){
			player.sendMessage("Player does not exist");
			return false;
		}
		if(playerbyname == null) {
			player.sendMessage("Player does not exist");
			return false;
		}
		
		//------------------ Initialize faction ------------------//
		Faction newfac = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		//------------------ Initialize player ------------------//
		PlayerCustom newpc = (PlayerCustom) readPCFile(playerbyname.getUniqueId().toString(),player);
		//------------- Faction compliance ---------------//
		if(newpc.fac_Name != playerCustom.fac_Name) {
			player.sendMessage("You cannot promote someone not in your "+commandName);
			return false;
		}
		if(newpc.fac_role == "owner") {
			player.sendMessage("You cannot promote the owner of your "+commandName);
			return false;
		}
		if(newpc.fac_role == "moderator" && playerCustom.fac_role == "moderator") {
			player.sendMessage("You cannot promote a fellow moderator");
			return false;
		}
		//--------------------- Change Faction and Player ----------------------//
		String currentRole = newpc.fac_role;
		if(newpc.fac_role == "peasant") {
			newfac.peasants.remove(playerbyname.getUniqueId());
			newfac.officer.add(playerbyname.getUniqueId());
			newpc.fac_role = "officer";
		}

		if(newpc.fac_role == "officer") {
			newfac.officer.remove(playerbyname.getUniqueId());
			newfac.moderator.add(playerbyname.getUniqueId());
			newpc.fac_role = "moderator";
		}
		
		if(newpc.fac_role == "moderator") {
			newfac.moderator.remove(playerbyname.getUniqueId());
			newfac.owner.add(playerbyname.getUniqueId());
			newpc.fac_role = "owner";
		}
		writeFile(newpc, playerbyname.getUniqueId().toString(), player);
		writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
		try {
			playerbyname.sendMessage("You have been promoted from "+currentRole+" to "+newpc.fac_role+". By player "+player.getName());
		}catch(Exception e) {}

		messageMembers(playerCustom, player, "Player "+args[1]+" has been promoted from "+currentRole+" to "+newpc.fac_role+". By player "+player.getName());
		return true;

	}
	
	private boolean demoteFaction(PlayerCustom playerCustom,String[] args, Player player) {
		//f demote name
		//f   0      1 	
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}

		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can demote "+commandName+" members");
			return false;
		}
			
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		Player playerbyname = null;
		try {
			playerbyname = (Player) Bukkit.getOfflinePlayer(getUUID(args[1]));
		}
		catch(Exception e){
			player.sendMessage("Player does not exist");
			return false;
		}
		if(playerbyname == null) {
			player.sendMessage("Player does not exist");
			return false;
		}
		
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		Faction newfac = faction;
		//------------------ Initialize player ------------------//
		PlayerCustom pc = (PlayerCustom) readPCFile(playerbyname.getUniqueId().toString(),player);
		PlayerCustom newpc = pc;
		//------------- Faction compliance ---------------//
		if(newpc.fac_Name != playerCustom.fac_Name) {
			player.sendMessage("You cannot demote someone not in your "+commandName);
			return false;
		}
		if(newpc.fac_role == "peasant") {
			player.sendMessage("You cannot demote a peasant");
			return false;
		}
		if(newpc.fac_role == "owner" && playerCustom.fac_role != "owner") {
			player.sendMessage("You cannot demote the owner of your "+commandName);
			return false;
		}
		if(newpc.fac_role == "owner" && playerCustom.fac_role == "owner" && newfac.owner.size() < 2) {
			player.sendMessage("You cannot demote yourself without another owner");
			return false;
		}
		if(newpc.fac_role == "moderator" && playerCustom.fac_role == "moderator") {
			player.sendMessage("You cannot demote a fellow moderator");
			return false;
		}
		//--------------------- Change Faction and Player ----------------------//
		String currentRole = newpc.fac_role;
		if(newpc.fac_role == "officer") {
			newfac.peasants.add(playerbyname.getUniqueId());
			newfac.officer.remove(playerbyname.getUniqueId());
			newpc.fac_role = "peasant";
		}
		
		if(newpc.fac_role == "moderator") {
			newfac.officer.add(playerbyname.getUniqueId());
			newfac.moderator.remove(playerbyname.getUniqueId());
			newpc.fac_role = "officer";
		}
		if(newpc.fac_role == "owner") {
			newfac.moderator.add(playerbyname.getUniqueId());
			newfac.owner.remove(playerbyname.getUniqueId());
			newpc.fac_role = "moderator";
		}
		writeFile(newpc, playerbyname.getUniqueId().toString(), player);
		writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
		
		playerbyname.sendMessage("You have been demoted from "+currentRole+" to "+newpc.fac_role+". By player "+player.getName());

		messageMembers(playerCustom, player, "Player "+args[1]+" has been demoted from "+currentRole+" to "+newpc.fac_role+". By player "+player.getName());
		return true;
	}
	private boolean openFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f open
		//f   0 
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}

		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can open a "+commandName);
			return false;
		}
			
		if(args.length < 1) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 1) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		Faction newfac = faction;
		newfac.type = "open";
		writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
		return true;
	}
	
	private boolean closeFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f close
		//f   0 
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}

		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can close a "+commandName);
			return false;
		}
			
		if(args.length < 1) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 1) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		Faction newfac = faction;
		//--------------------- Change Faction and Player ----------------------//
		newfac.type = "close";
		writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
		return true;
	}
	private boolean allyFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f ally name
		//f   0   1 
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}

		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can ask a "+commandName+" to ally");
			return false;
		}
			
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		Faction newfac = faction;

		//------------- Faction compliance ---------------//
		File facFile = new File(args[1].toLowerCase()+".json");
		try {
			if(facFile.createNewFile()) {
				facFile.delete();
				player.sendMessage(commandName+" does not exist");
				return false;
			}
		}
		catch(Exception e) {
			player.sendMessage("Name compliance error");
			return false;
		}
		if(newfac.ally.contains(args[1].toLowerCase())) {
			player.sendMessage(commandName+" already allied");
			return false;
		}

		//--------------------- Change Faction and Player ----------------------//
		Faction opposingfaction = (Faction) readFacFile(args[1].toLowerCase(),player);
		if(opposingfaction.allyinvite.contains(playerCustom.fac_Name.toLowerCase())) {
			opposingfaction.allyinvite.remove(playerCustom.fac_Name.toLowerCase());
			opposingfaction.ally.add(playerCustom.fac_Name);
			try {
				opposingfaction.enemy.remove(playerCustom.fac_Name.toLowerCase());
				newfac.enemy.remove(args[1].toLowerCase());
			}catch(Exception e) {}
			newfac.ally.add(args[1].toLowerCase());
			player.sendMessage("Added "+commandName+" as ally");
			messageMembers(playerCustom, player, commandName+" "+args[1]+" and "+commandName+playerCustom.fac_Name+" are now Allies");
			messageMembers((PlayerCustom) readPCFile(opposingfaction.owner.get(0).toString(),player), player, commandName+" "+args[1]+" and "+commandName+playerCustom.fac_Name+" are now Allies");
			writeFile(opposingfaction, args[1].toLowerCase(), player);
			writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
			return true;
		}
		else if(!newfac.allyinvite.contains(args[1].toLowerCase())){
			newfac.allyinvite.add(args[1].toLowerCase());
			player.sendMessage(args[1] + " has been invited to ally");
			writeFile(opposingfaction, args[1].toLowerCase(), player);
			writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
			return true;
		}
		else {
			player.sendMessage("This "+commandName+" has already been invited to be allies");
			return true;
		}
		
	}
	
	private boolean enemyFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f enemy name
		//f   0   1 
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}

		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can set a "+commandName+" to enemy");
			return false;
		}
			
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		Faction newfac = faction;
		
		//------------- Faction compliance ---------------//
		File facFile = new File(args[1].toLowerCase()+".json");
		try {
			if(facFile.createNewFile()) {
				facFile.delete();
				player.sendMessage(commandName+" does not exist");
				return false;
			}
		}
		catch(Exception e) {
			player.sendMessage("Name compliance error");
			return false;
		}
		if(newfac.enemy.contains(args[1].toLowerCase())){
			player.sendMessage("This "+commandName+" is already your enemy");
			return false;
		}
		
		//--------------------- Change Faction and Player ----------------------//
		Faction opposingfaction = (Faction) readFacFile(args[1].toLowerCase(),player);
		//
		try {
			newfac.ally.remove(args[1].toLowerCase());
			opposingfaction.ally.remove(playerCustom.fac_Name.toLowerCase());
		}
		catch(Exception e){}
		newfac.enemy.add(args[1].toLowerCase());
		opposingfaction.enemy.add(playerCustom.fac_Name.toLowerCase());
		writeFile(opposingfaction, args[1].toLowerCase(), player);
		writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
		messageMembers(playerCustom, player, commandName+" "+args[1]+" and "+commandName+playerCustom.fac_Name+" are now Enemies");
		messageMembers((PlayerCustom) readPCFile(opposingfaction.owner.get(0).toString(),player), player, commandName+" "+args[1]+" and "+commandName+playerCustom.fac_Name+" are now Enemies");
		//TODO reload name changer
		return true;	
	}
	
	private boolean neutralFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f enemy name
		//f   0   1
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}

		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can set a "+commandName+" to neutral");
			return false;
		}
			
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		Faction newfac = faction;
		//--------------------- Change Faction and Player ----------------------//
		File facFile = new File(args[1].toLowerCase()+".json");
		try {
			if(facFile.createNewFile()) {
				facFile.delete();
				player.sendMessage(commandName+" does not exist");
				return false;
			}
		}
		catch(Exception e) {
			player.sendMessage("Name compliance error");
			return false;
		}

		if(newfac.nuinvite.contains(args[1].toLowerCase())) {
			player.sendMessage(commandName+" already allied");
			return false;
		}
		//------------------ Initialize faction ------------------//
		Faction opposingfaction = (Faction) readFacFile(args[1].toLowerCase(),player);
		if(opposingfaction.nuinvite.contains(playerCustom.fac_Name.toLowerCase())) {
			try {
				opposingfaction.enemy.remove(playerCustom.fac_Name.toLowerCase());
				newfac.enemy.remove(args[1].toLowerCase());
			}catch(Exception e) {}
			try {
				opposingfaction.ally.remove(playerCustom.fac_Name.toLowerCase());
				newfac.ally.remove(args[1].toLowerCase());
			}catch(Exception e) {}
			
			messageMembers(playerCustom, player, commandName+" "+args[1]+" and "+commandName+playerCustom.fac_Name+" are now Neutral");
			messageMembers((PlayerCustom) readPCFile(opposingfaction.owner.get(0).toString(),player), player, commandName+" "+args[1]+" and "+commandName+playerCustom.fac_Name+" are now Neutral");
			
			writeFile(opposingfaction, args[1].toLowerCase(), player);
			writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
			return true;
		}
		else if(!newfac.nuinvite.contains(args[1].toLowerCase())){
				newfac.nuinvite.add(args[1].toLowerCase());
				player.sendMessage(args[1] + " has been asked to be neutral");
				writeFile(opposingfaction, args[1].toLowerCase(), player);
				writeFile(newfac, playerCustom.fac_Name.toLowerCase(), player);
				return true;
			}
		else {
			player.sendMessage("This "+commandName+" has already been invited to be neutral");
			return true;
		}
	}
	private boolean whoFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f ally name
		//f   0   1 
		
		//------------- Faction compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}
			
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		
		//------------- Faction compliance ---------------//
		File facFile = new File(args[1].toLowerCase()+".json");
		try {
			if(facFile.createNewFile()) {
				facFile.delete();
				player.sendMessage(commandName+" does not exist");
				return false;
			}
		}
		catch(Exception e) {
			player.sendMessage("Name compliance error");
			return false;
		}
		//--------------------- Change Faction and Player ----------------------//
		Faction opposingfaction = (Faction) readFacFile(args[1].toLowerCase(),player);
		player.sendMessage(
				"-".repeat(opposingfaction.name.length()+2)
				+ "\t\t" + opposingfaction.name
				+ "\n"
				+ "-".repeat(opposingfaction.name.length()+2)
				+ "\nDescription: \n"
				+ opposingfaction.desc
				+ "\n" 
				+ "Members: \n"
				+ "\n\tOwner: \n"
				+ opposingfaction.owner.toString() 
				+ "\n\tModerator: \n"
				+ ((opposingfaction.moderator.size() > 0) ? opposingfaction.moderator.toString() : "No Moderators")
				+ "\n\tOfficer: \n"
				+ ((opposingfaction.officer.size() > 0) ? opposingfaction.officer.toString() : "No Officers")
				+ "\n\tPeasants: \n"
				+ ((opposingfaction.peasants.size() > 0) ? opposingfaction.peasants.toString() : "No Peasants")
				+ "-".repeat(opposingfaction.name.length()+2)
		);
		return true;
	}
	
	private boolean nameFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f name <name>
		//f   0    1
		
		//------------- Faction creation compliance ---------------//
		if(!playerCustom.infac) {
			player.sendMessage("You are not in a "+commandName);
			return false;
		}
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(playerCustom.fac_role != "owner") {
			player.sendMessage("Only members higher than officer can set a "+commandName+" to neutral");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		//------------------ Initialize faction ------------------//
		File oldFile = new File(playerCustom.fac_Name.toLowerCase()+".json");
		File facFile = new File(args[1].toLowerCase()+".json");
		try {
			if(!facFile.createNewFile()) {
				player.sendMessage("This "+commandName+" already exists");
				return false;
			}
		}
		catch (IOException e) {
			player.sendMessage("This is not a compliant name.");
			return false;
		}
		//--------------------- Change Player JSON ----------------------//
		if(oldFile.renameTo(facFile)) {
			player.sendMessage("Faction successfully renamed.");
			messageMembers(playerCustom, player, "Your Faction name has been changed to "+args[1]);
			return false;
		}
		else{
			player.sendMessage("File could not be renamed. This may be a serious issue.");
			return false;
		}
	}

	private boolean descFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f desc description 
		//f   0     1
		
		//------------- Faction compliance ---------------//
		if(playerCustom.infac) {
			player.sendMessage("You are in a "+commandName+" already");
			return false;
		}
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can set a "+commandName+" to change the description");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		//------------------ Initialize faction ------------------//
		Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		try {

			for(int i = 0; i+1 < args.length; i++) {
				faction.desc = faction.desc + args[i] + " ";
			}
			writeFile(faction, playerCustom.fac_Name.toLowerCase(), player);
			player.sendMessage("Description has changed to "+faction.desc);
			return true;
		}
		catch(Exception e) {
			player.sendMessage("Description compliance error");
			return false;
		}
	}
	private boolean bannerFaction(PlayerCustom playerCustom, String[] args, Player player) {
		//f banner
		//f   0    
		
		//------------- Faction compliance ---------------//
		if(playerCustom.infac) {
			player.sendMessage("You are in a "+commandName+" already");
			return false;
		}
		if(args.length < 2) {
			player.sendMessage(NOT_ENOUGH_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		if(playerCustom.fac_role != "owner" || playerCustom.fac_role != "moderator") {
			player.sendMessage("Only members higher than officer can set a "+commandName+" to change the banner");
			return false;
		}
		if(args.length > 2) {
			player.sendMessage(TOO_MANY_ARGUMENTS);
			player.sendMessage("/help");
			return false;
		}
		//
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!item.getType().name().contains("BANNER")){
			player.sendMessage("Please hold the banner you would like to change the banner to");
			return false;
		}
		else {
			Faction faction = (Faction) readFacFile(playerCustom.fac_Name.toLowerCase(),player);
			BannerMeta itemMeta = (BannerMeta) player.getInventory().getItemInMainHand().getItemMeta();
			item.setItemMeta(itemMeta);
			faction.bannerLook = item;
			writeFile(faction, playerCustom.fac_Name.toLowerCase(), player);
			return true;
		}
	}
}