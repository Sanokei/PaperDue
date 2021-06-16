package github.sanokei.paperdue.banner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.persistence.PersistentDataType;

import github.sanokei.paperdue.files.Faction;
import github.sanokei.paperdue.files.PlayerCustom;
import github.sanokei.paperdue.Main;
import github.sanokei.paperdue.factions.FactionsCommand;

@SuppressWarnings("unused")
public class BannerListener implements Listener{
	
	boolean isBanner(ItemStack i) {
		//Bukkit no longer has one specific Material for BANNER, so to combat this, we check for "BANNER" in the material name instead
        return i != null && i.getType().name().contains("BANNER");
    }
	@EventHandler
	public void onBannerPlace(BlockPlaceEvent e) {
		if(isBanner(e.getItemInHand())){
			BannerMeta itemMeta = (BannerMeta) e.getItemInHand().getItemMeta();
			NamespacedKey facBanner = new NamespacedKey(Main.getPlugin(), "banner_ID");
			if(itemMeta.getPersistentDataContainer().has(facBanner, PersistentDataType.INTEGER)){
				/*
				 * im debating if i should cancel the block place event and handle it on my own or just use the ingame features to test everything i need to test
				 * im tired so i think imma go with the lazy way. I just cant have the item drop..
				 * */
				e.getBlock().setType(Material.AIR);
				PlayerCustom playerCustom = (PlayerCustom) FactionsCommand.readPCFile(e.getPlayer().getUniqueId().toString(),e.getPlayer());
				Faction faction = (Faction) FactionsCommand.readFacFile(playerCustom.fac_Name.toLowerCase(),e.getPlayer());
				e.setCancelled(true); //has to cancel no matter the outcome
				if(playerCustom.infac) {
					Block underBlock = e.getBlock();
					Block overBlock = e.getBlock();
					underBlock.getLocation().setY(underBlock.getLocation().getY()-1);
					overBlock.getLocation().setY(overBlock.getLocation().getY()+1);
					/*
					 * [FUTURE PROOFING] 1.16 -> 1.17
					 * getMaxHeight 
					 * returns the height limit of the world as a non programming number (not using 0)
					 * so a limit of 100 would mean a y value of 0 to 99
					 * since a banner is 2 blocks high this should not matter.
					 * e.getPlayer().getWorld().getMaxHeight() would return 256 for the main 1.16.x world (note: 1.17 changes height limit)
					 * */
					//I dont even need that since you can still attack entities at world height
					Material matUnder = underBlock.getType();
					Material matOver = overBlock.getType();
					Banner newBanner = new Banner(Main.getPlugin());
					Location location = new Location(e.getPlayer().getWorld(),e.getBlock().getX(),e.getBlock().getY(),e.getBlock().getZ());
					//Create banner at location
					newBanner.CreateBanner(e.getPlayer(), faction.bannerLook, itemMeta, location);
				}
				else {
					e.getPlayer().sendMessage("You must be part of a "+ (FactionsCommand.commandName+" to place a banner"));
				}
			}
		}
	}
	
	@EventHandler
	public void onBannerInteract(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof ArmorStand) {
			NamespacedKey armorStand = new NamespacedKey(Main.getPlugin(), "banner_ID");
			if(e.getRightClicked().getPersistentDataContainer().has(armorStand,PersistentDataType.INTEGER)) {
				//TODO make menu
			}
		}
	}
	
	//TODO when making banner attacked test if banner is part of faction or not
}
