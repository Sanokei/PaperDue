package github.sanokei.paperdue.banner;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;

import github.sanokei.paperdue.Main;
import github.sanokei.paperdue.factions.FactionsCommand;
import github.sanokei.paperdue.files.Faction;
import github.sanokei.paperdue.files.PlayerCustom;
import github.sanokei.paperdue.util.Ray;
import net.minecraft.world.level.material.Material;


/*TODO
 * customizable base plates for the different levels
 * level based base plates so you know what level the banner is
 * i.e bottom head part wood or quartz then more elaborate etc
 * */
public class Banner {
	/*TODO
	 * make 2 
	 * location
	 * faction
	 * date
	 * uuid - number
	 * health
	 */
	
	//glowing magma cube
	//banner
	//~ ~-1.3 ~0.25
	//armor stand
	//~ ~0.5 ~
	public Plugin plugin;
	//The item
	public Banner(Plugin plugin) {this.plugin=plugin;}
	
	public ArmorStand createArmorStand(Location location, String name, boolean visible, boolean arms, boolean mini, boolean nameVis, boolean marker, boolean fire, boolean grav, boolean invul) {
		ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
		if(name != null)
			as.setCustomName(name);
        as.setArms(arms);
        as.setVisible(visible);
        
        //TODO set false, set up listener for attacked
        as.setInvulnerable(invul);
        
        as.setGravity(grav);
        as.setSmall(mini);
        as.setMarker(marker);
        as.setVisualFire(fire);//Makes bright
        //
        as.setBasePlate(false);
        as.setCanPickupItems(false);
		return as;
	}
	public ArmorStand equipLock(ArmorStand bannerAs) {
		bannerAs.addEquipmentLock(EquipmentSlot.HEAD,ArmorStand.LockType.REMOVING_OR_CHANGING);
		bannerAs.addEquipmentLock(EquipmentSlot.CHEST,ArmorStand.LockType.REMOVING_OR_CHANGING);
		bannerAs.addEquipmentLock(EquipmentSlot.FEET,ArmorStand.LockType.REMOVING_OR_CHANGING);
		bannerAs.addEquipmentLock(EquipmentSlot.HAND,ArmorStand.LockType.REMOVING_OR_CHANGING);
		bannerAs.addEquipmentLock(EquipmentSlot.LEGS,ArmorStand.LockType.REMOVING_OR_CHANGING);
		bannerAs.addEquipmentLock(EquipmentSlot.OFF_HAND,ArmorStand.LockType.REMOVING_OR_CHANGING);
		return bannerAs;
	}
	//creates a banner entity with one of the items being the banner
	public boolean CreateBanner(Player player, ItemStack bannerCustom, BannerMeta itemMeta, Location location) {
		//TODO make this NMS packets so that i can just send packets to every player instead
		bannerCustom.setItemMeta(itemMeta);
		//Location
		Location bannerAsLocation = location.clone().add(0,1.3, 0.25); //location is of the block itself meaning i must set the offset
		Location hitDetection = location.clone().add(0,0.5,0);
		//ArmorStand
		ArmorStand bannerAs = createArmorStand(bannerAsLocation, null, false,false,false,false,true,false,false,true);
		ArmorStand hitBox = createArmorStand(bannerAsLocation, null, false,false,false,false,false,true,false,false);
		//NamespacedKey
		NamespacedKey facBannerHealth = new NamespacedKey(Main.getPlugin(), "banner_health");
		NamespacedKey facBannerID = new NamespacedKey(Main.getPlugin(), "banner_ID");
		/*------PlayerCustom and Faction------*/
		PlayerCustom playerCustom = (PlayerCustom) FactionsCommand.readPCFile(player.getUniqueId().toString(),player);
		Faction faction = (Faction) FactionsCommand.readFacFile(playerCustom.fac_Name.toLowerCase(),player);
		/*------Set PersistentDataContainer------*/
		//set Id of the banner for the size of the array
		hitBox.getPersistentDataContainer().set(facBannerID, PersistentDataType.INTEGER,faction.bannerID.size());
		//lock equip
		bannerAs = equipLock(bannerAs);
		hitBox = equipLock(hitBox);
		//set health of banner
		//TODO
		//Set head of banner
		EntityEquipment eq = bannerAs.getEquipment();
		eq.setHelmet(bannerCustom);
		
		return false;
	}
	
	
	
}
