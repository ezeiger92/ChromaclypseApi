package com.chromaclypse.api.menu;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.chromaclypse.api.annotation.Mutable;
import com.chromaclypse.api.annotation.Nullable;
import com.chromaclypse.api.item.ItemBuilder;

public class Menu implements InventoryHolder {
	private Inventory inv;
	private Consumer<InventoryClickEvent>[] buttons;

	@SuppressWarnings("unchecked")
	public Menu(int rows, String title) {
		inv = Bukkit.createInventory(this, rows * 9, title);
		buttons = new Consumer[inv.getSize()];
	}
	
	public void edit(int rows, String title) {
		int cells = rows * 9;
		int copy = Math.min(cells, inv.getSize());
		List<HumanEntity> viewers = inv.getViewers();
		Inventory bak = inv;
		
		inv = Bukkit.createInventory(this, cells, title);
		
		for(int i = 0; i < copy; ++i)
			inv.setItem(i, bak.getItem(i));
		
		for(HumanEntity viewer : viewers)
			viewer.openInventory(inv);
		
		if(cells != buttons.length) {
			@SuppressWarnings("unchecked")
			Consumer<InventoryClickEvent>[] b2 = new Consumer[cells];
			
			for(int i = 0; i < copy; ++i)
				b2[i] = buttons[i];
			
			buttons = b2;
		}
	}
	
	public void put(int index, @Mutable ItemStack item, Consumer<InventoryClickEvent> handler) {
		inv.setItem(index, ItemBuilder.edit(item).flag(ItemFlag.HIDE_PLACED_ON).get());
		buttons[index] = handler;
	}
	
	public @Nullable Consumer<InventoryClickEvent> getHandler(int index) {
		return buttons[index];
	}
	
	@Override
	public Inventory getInventory() {
		return inv;
	}
}
