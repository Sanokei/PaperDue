package net.paperdue.factions.files;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.annotations.SerializedName;

public class Faction {
	//Basic
	public String name;
	@SerializedName("description")
	public String desc;
	public String type;
	public String date;
	//Member
	public Map<UUID,String> members = new LinkedHashMap<>();
	public List<UUID> owner = new ArrayList<>();
	public List<UUID> moderator = new ArrayList<>();
	public List<UUID> officer = new ArrayList<>();
	public List<UUID> peasants = new ArrayList<>();
	//Invited
	public List<UUID> invited =  new ArrayList<>();
	//Banned
	//if open then you can still ban people from the fac
	public List<UUID> banned =  new ArrayList<>();
	//Enemy and Ally
	public List<String> ally =  new ArrayList<String>();
	public List<String> allyinvite = new ArrayList<String>();
	public List<String> nuinvite = new ArrayList<String>();
	public List<String> enemy =  new ArrayList<String>();
	//Banners
	public List<Integer> bannerID =  new ArrayList<Integer>(); //armorStand_ID
	public ItemStack bannerLook = new ItemStack(Material.WHITE_BANNER);
	//
	private DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	
	//main JSON to add to to add to the json later with UUID
	
	public Faction(String[] args,Player player) {
		this.name = args[1];
		try {
			for(int i = 0; i+2 < args.length; i++) {
				this.desc = this.desc + args[i] + " ";
			}
		}
		catch(Exception e) {
			this.desc = "No description yet ;-;";
		}
		this.type = "closed";
		this.owner.add(player.getUniqueId());
		this.members.put(player.getUniqueId(),player.getDisplayName());
		df.setTimeZone(TimeZone.getTimeZone("Japan"));
		this.date = df.format(new Date());
	}

	
}
