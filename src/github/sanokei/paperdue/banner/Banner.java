package github.sanokei.paperdue.banner;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.libs.org.codehaus.plexus.util.ReflectionUtils;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import github.sanokei.paperdue.Main;
import net.minecraft.core.IRegistry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.monster.EntityMagmaCube;


/*TODO
 * customizable base plates for the different levels
 * level based base plates so you know what level the banner is
 * i.e bottom head part wood or quartz then more elaborate etc
 * */
//TODO banner decay
//TODO "fainted" banner / broken
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
	public boolean createBanner(Player player, ItemStack bannerCustom, BannerMeta itemMeta, Location location) {
		try {
			/*
			 * TODO
			 * create the banners
			 * create hitbox
			 * create damage counter at the top
			 * create slime
			 * */
			//TODO make this NMS packets so that i can just send packets to every player instead
			//bannerCustom.setItemMeta(itemMeta);
			
			//Location
			Location bannerAsLocation = location.clone().add(0,1.3, 0.25); //location is of the block itself meaning i must set the offset
			Location hitDetection = location.clone().add(0,0.5,0);
			//ArmorStand
			ArmorStand bannerAs = createArmorStand(bannerAsLocation, null, false,false,false,false,true,false,false,true);
			ArmorStand hitBox = createArmorStand(hitDetection, null, false,false,false,false,false,true,false,false);
			//NamespacedKey
			NamespacedKey facBannerHealth = new NamespacedKey(Main.getPlugin(), "banner_health");
			/*------Set PersistentDataContainer------*/
			//set Id of the banner for the size of the array
			
			//we dont create a new id cuz the item should have one already
			//itemMeta.getPersistentDataContainer().set(facBannerID, PersistentDataType.INTEGER,faction.bannerID.size());
			
			//lock equip
			bannerAs = equipLock(bannerAs);
			hitBox = equipLock(hitBox);
			//set health of banner
			//if(getMaxHealthBanner(hitBox) != 0) {
			//getMaxHealthBanner(hitBox);
			//}
			hitBox.setCustomName(itemMeta.getPersistentDataContainer().get(facBannerHealth, PersistentDataType.FLOAT).toString());
			
			//set the item meta
			bannerCustom.setItemMeta(itemMeta);
			//Set head of banner
			EntityEquipment eq = bannerAs.getEquipment();
			eq.setHelmet(bannerCustom);
			//create magma cube
			createClaimBorder();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	
	private void createClaimBorder() {
		// TODO Auto-generated method stub
		
	}
	public void spawnBorder(Location loc, Player p) {
//        WorldServer s = ((CraftWorld)loc.getWorld()).getHandle();
//        EntityType<MAGMA_CUBE> BORDER_SLIME = ENTITIES.register(EntityNames.MAGMA_CUBE,()-> EntityMagmaCube::new, EntityClassification.MONSTER, 2.04F, 2.04F);
//        EntityMagmaCube cube = new EntityMagmaCube( s);
//        ReflectionUtils.sendPacket(p, packet);
	    //TODO create the border thing
	}
	public void damageBanner(float damage, ArmorStand as) {
		if(getBanner(as) != null) {
			NamespacedKey facBannerHealth = new NamespacedKey(Main.getPlugin(), "banner_health");
			ItemStack banner = getBanner(as);
			Float currentHealth = banner.getItemMeta().getPersistentDataContainer().get(facBannerHealth, PersistentDataType.FLOAT);
			//takes the health and subtracts the damage it recieves as a parameter
			banner.getItemMeta().getPersistentDataContainer().set(facBannerHealth, PersistentDataType.FLOAT, currentHealth - damage);
		}
	}
	public float getMaxHealthBanner(ArmorStand as) {
		//it would need to calculate the health of the banner using levels of the banner
		if(getBanner(as) != null) {
			NamespacedKey facBannerLevel = new NamespacedKey(Main.getPlugin(), "banner_level");
			ItemStack banner = getBanner(as);
			return levelToHealth(banner.getItemMeta().getPersistentDataContainer().get(facBannerLevel, PersistentDataType.INTEGER));
		}
		return 0;
	}
	public int levelToHealth(int level) {
		if(level == 0) {
			return 20; //hearts would be like 10 hearts
		}
		return (int)Math.log((level + 1)*100);
	}
	public ItemStack getBanner(ArmorStand as) {
		NamespacedKey facBannerID = new NamespacedKey(Main.getPlugin(), "banner_ID");
		EntityEquipment eq = as.getEquipment();
		if(BannerListener.isBanner(eq.getHelmet())) {
			if(eq.getHelmet().getItemMeta().getPersistentDataContainer().has(facBannerID, PersistentDataType.INTEGER)) {
				return eq.getHelmet();
			}
		}
		return null;
	}
}
