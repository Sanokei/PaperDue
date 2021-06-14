package github.sanokei.paperdue.banner;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.NBTTagCompound;

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
	
	public Plugin plugin;
	//The item
	public Banner(Plugin plugin) {this.plugin=plugin;}
	
	private ArmorStand createArmorStand(Location location, String name, boolean visible, boolean arms, boolean mini, boolean nameVis) {
		ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
		if(name != null)
			as.setCustomName(name);
        as.setBasePlate(false);
        as.setArms(arms);
        as.setVisible(visible);
        as.setInvulnerable(true);
        as.setCanPickupItems(false);
        as.setGravity(false);
        as.setSmall(mini);
		return as;
	}
	//creates a banner entity with one of the items being the banner
	public boolean CreateBanner(Player player, ItemStack bannerCustom, BannerMeta itemMeta, Location location) {
		//location is of the block itself meaning i must set the offset 
		Location bannerAsLocation = location.clone().add(0,0, 0);
		ArmorStand bannerAs = createArmorStand(bannerAsLocation, false,false,false);
		ArmorStand hitboxAs = createArmorStand(location,false,false,false);
		BoundingBox bb = new BoundingBox(location.getX(),location.getY(),location.getZ(),location.getX(),location.getY()+1,location.getZ());
		
		return false;
	}
	
	
	
}
