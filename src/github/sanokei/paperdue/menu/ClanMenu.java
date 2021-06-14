package github.sanokei.paperdue.menu;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;



public class ClanMenu extends Menu{
	private String menuName;
	private int slots;
	private ArrayList<String> reward_lore;
	
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public void setSlots(int slots) {
		this.slots = slots;
	}
	public void setReward_Lore(ArrayList<String> reward) {
		this.reward_lore = reward;
	}
	public ClanMenu(Player player,String menuName, int slots, ArrayList<String> reward) {
		setMenuName(menuName);
		setSlots(slots);
		setReward_Lore(reward);
		getPlayer(player);
	}
	
	@Override
	protected Player getPlayer(Player player) {
		return player;
		
	}

	@Override
	public String getMenuName() {
		return menuName;
	}

	@Override
	public int getSlots() {
		return slots;
	}


	@Override
	public void handleMenu(InventoryClickEvent e) {
		switch(e.getCurrentItem().getType()){
			case GREEN_STAINED_GLASS_PANE:
				e.getWhoClicked().closeInventory();
				e.getWhoClicked().sendMessage(ChatColor.GREEN+"Good luck on your quest");
				break;
			case RED_STAINED_GLASS_PANE:
				e.getWhoClicked().sendMessage(ChatColor.GREEN+"Come back if you want to start");
				e.getWhoClicked().closeInventory();
				break;
			default:
				break;
		}
	}

	@Override
	public void setMenuItems() {
		ItemStack accept = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
		ItemMeta accept_meta = accept.getItemMeta();
		ArrayList<String> accept_lore = new ArrayList<>();
		//Display name
		accept_meta.setDisplayName(ChatColor.GREEN + "Accept");
		//Lore
		accept_lore.add(ChatColor.AQUA+"Accept the quest?");
		//
		accept_meta.setLore(accept_lore);
		/**/
		ItemStack deny = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta deny_meta = deny.getItemMeta();
		ArrayList<String> deny_lore = new ArrayList<>();
		//Display name
		deny_meta.setDisplayName(ChatColor.DARK_RED + "Deny");
		//Lore
		deny_lore.add(ChatColor.AQUA+"Deny the quest?");
		//
		deny_meta.setLore(deny_lore);
		/**/
		ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
		ItemMeta filler_meta = filler.getItemMeta();
		ArrayList<String> filler_lore = new ArrayList<>();
		//Display name
		filler_meta.setDisplayName("");
		//Lore
		filler_lore.add(ChatColor.AQUA+"");
		//
		filler_meta.setLore(filler_lore);
		/**/
		/**/
		ItemStack reward = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
		ItemMeta reward_meta = reward.getItemMeta();
		//Display name
		reward_meta.setDisplayName("Reward for completion");
		reward_meta.setLore(reward_lore);
		/**/
		inventory.setItem(0, filler);
		inventory.setItem(1, filler);
		inventory.setItem(2, filler);
		inventory.setItem(3, accept);
		inventory.setItem(4, filler);
		inventory.setItem(5, reward);
		inventory.setItem(6, filler);
		inventory.setItem(7, deny);
		inventory.setItem(8, filler);
		inventory.setItem(9, filler);
		inventory.setItem(10, filler);
		
		
	}
}
