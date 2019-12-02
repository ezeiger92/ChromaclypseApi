package com.chromaclypse.api.messages;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.chromaclypse.api.item.ItemBuilder;
import com.chromaclypse.api.json.JsonBlob;

public class Messager {
	
	public static void sendBook(Player player, JsonBlob page, JsonBlob... extra) {
		if(player == null) {
			throw new NullPointerException("Player cannot be null");
		}
		
		player.openBook(new ItemBuilder(Material.WRITTEN_BOOK).pages(page, extra).get());
	}
}
