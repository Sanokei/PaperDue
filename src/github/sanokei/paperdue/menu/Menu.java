package github.sanokei.paperdue.menu;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import github.sanokei.paperdue.Main;

public abstract class Menu implements InventoryHolder{
	
	protected Inventory inventory;
	
	protected Player player;

	protected abstract Player getPlayer(Player player);

	public abstract String getMenuName();
	
	public abstract int getSlots();
	
	public abstract void handleMenu(InventoryClickEvent e);
	
	public abstract void setMenuItems();
	
	public void open(){
		inventory = Bukkit.createInventory(this, getSlots(),getMenuName());
		
		this.setMenuItems();
		
		player.openInventory(inventory);
	}
	
	@Override
	public Inventory getInventory(){
		return this.inventory;
	}
	
}
