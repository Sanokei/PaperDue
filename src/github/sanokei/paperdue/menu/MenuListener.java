package github.sanokei.paperdue.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import github.sanokei.paperdue.Main;
import github.sanokei.paperdue.util.NearestEntityInSight;

public class MenuListener implements Listener{
@SuppressWarnings("unused")
private Main plugin;
	
	public MenuListener(Main plugin){
		setPlugin(plugin);
	}

	private void setPlugin(Main plugin) {
		this.plugin = plugin;
		
	}

	@EventHandler
	public void inciciateJoinGroup(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
			Player player = event.getPlayer();
			if((event.getPlayer().hasPermission("clan.start") || event.getPlayer().hasPermission("clan.leader")) && player.isSneaking()){
				Player nearestPlayer;
				if(player.getInventory().getItemInMainHand().getType() == Material.AIR){
					nearestPlayer = NearestEntityInSight.nearestPlayerInSight(player);
					// Hit the closest player
          if (nearestPlayer != null) {
            //nearestPlayer.damage(5, player); //test
          	//start menu
	        }
		      }
		    }
			}
	}

}
