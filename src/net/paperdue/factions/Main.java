package net.paperdue.factions;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.paperdue.factions.commands.FactionsCommand;

/*------------------------------------------------------------------------------------------------------------------------
          _____                    _____                    _____                   _______         
         /\    \                  /\    \                  /\    \                 /::\    \        
        /::\    \                /::\    \                /::\____\               /::::\    \       
       /::::\    \              /::::\    \              /::::|   |              /::::::\    \      
      /::::::\    \            /::::::\    \            /:::::|   |             /::::::::\    \     
     /:::/\:::\    \          /:::/\:::\    \          /::::::|   |            /:::/~~\:::\    \    
    /:::/__\:::\    \        /:::/__\:::\    \        /:::/|::|   |           /:::/    \:::\    \   
    \:::\   \:::\    \      /::::\   \:::\    \      /:::/ |::|   |          /:::/    / \:::\    \  
  ___\:::\   \:::\    \    /::::::\   \:::\    \    /:::/  |::|   | _____   /:::/____/   \:::\____\ 
 /\   \:::\   \:::\    \  /:::/\:::\   \:::\    \  /:::/   |::|   |/\    \ |:::|    |     |:::|    |
/::\   \:::\   \:::\____\/:::/  \:::\   \:::\____\/:: /    |::|   /::\____\|:::|____|     |:::|    |
\:::\   \:::\   \::/    /\::/    \:::\  /:::/    /\::/    /|::|  /:::/    / \:::\    \   /:::/    / 
 \:::\   \:::\   \/____/  \/____/ \:::\/:::/    /  \/____/ |::| /:::/    /   \:::\    \ /:::/    /  
  \:::\   \:::\    \               \::::::/    /           |::|/:::/    /     \:::\    /:::/    /   
   \:::\   \:::\____\               \::::/    /            |::::::/    /       \:::\__/:::/    /    
    \:::\  /:::/    /               /:::/    /             |:::::/    /         \::::::::/    /     
     \:::\/:::/    /               /:::/    /              |::::/    /           \::::::/    /      
      \::::::/    /               /:::/    /               /:::/    /             \::::/    /       
       \::::/    /               /:::/    /               /:::/    /               \::/____/        
        \::/    /                \::/    /                \::/    /                 --              
         \/____/                  \/____/                  \/____/                                                                                                                                    
------------------------------------------------------------------------------------------------------------------------*/
public class Main extends JavaPlugin{
	//public WorldGuardPlugin wgp;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		this.getCommand("clan").setExecutor(new FactionsCommand(this));
		//wgp = getWorldGuard();
		plugin = this;
	}
	public static Main plugin;
	public static Plugin getPlugin() {
		return (Plugin) plugin;
	}
//	public WorldGuardPlugin getWorldGuard() {
//		Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
//		if(plugin == null || !(plugin instanceof WorldGuardPlugin)) {
//			return null;
//		}
//		return (WorldGuardPlugin) plugin;
//	}
}
/*TODO MUST FINISH
 Factions
	 -who
	 -name
	 -desc
 
 Flags
	 -Left click flag
	 	+attackable entity
	 -right click flag
		 +add player
		 +(later)upgrade
 	 -
 
 
 
 
 
 
 
 */

/*TODO Optional
 Optimize code
 -sub commands
 
 Metrics
 
 
 
 
 
 
 */