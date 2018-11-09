package com.chromaclypse.api.messages;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.chromaclypse.api.Chroma;
import com.chromaclypse.api.Reflect;
import com.chromaclypse.api.item.ItemBuilder;
import com.chromaclypse.api.json.JsonBlob;

public class Messager {
	public static final String BOOK_CHANNEL = "minecraft:book_open";
	
	public static void sendBook(Player player, JsonBlob page, JsonBlob... extra) {
		if(player == null) {
			throw new NullPointerException("Player cannot be null");
		}
		
		boolean listening = player.getListeningPluginChannels().contains(BOOK_CHANNEL);
		
		if(!listening) {
			try {
				Reflect.playerAddChannel(player, BOOK_CHANNEL);
			}
			catch(Exception e) {
				throw new IllegalStateException("Failed to open plugin channel for " + player.getName(), e);
			}
		}
		
		ItemStack old = player.getInventory().getItemInMainHand();
		ItemStack book = new ItemBuilder(Material.WRITTEN_BOOK).pages(page, extra).get();
		byte[] payload = { 0 };
		
		player.getInventory().setItemInMainHand(book);
		
		try {
			player.sendPluginMessage(Chroma.get().plugin(), BOOK_CHANNEL, payload);
		}
		catch(Exception e) {
			throw new IllegalStateException("Could not send plugin message", e);
		}
		finally {
			player.getInventory().setItemInMainHand(old);
			
			if(!listening) {
				try {
					Reflect.playerRemoveChannel(player, BOOK_CHANNEL);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
