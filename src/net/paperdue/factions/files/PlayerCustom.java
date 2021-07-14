package net.paperdue.factions.files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PlayerCustom {
	public boolean infac = false; 
	@SerializedName("Homes")
	private Map<String,Object> homes = new LinkedHashMap<>();
	@SerializedName("HomesAllowed")
	private int allowedHomes = 1;
	@SerializedName("TagOwned")
	private List<String> tag_owned = new ArrayList<>(); 
	@SerializedName("TagCurrent")
	private String tag_current;
	@SerializedName("FactionName")
	public String fac_Name;
	@SerializedName("FactionRole")
	public String fac_role;
	
	public PlayerCustom(){
	}
	
}