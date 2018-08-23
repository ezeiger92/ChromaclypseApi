package com.chromaclypse.api.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

public class Clicks {
	private Clicks() {
	}

	public static int number(InventoryClickEvent event, int amount, int shiftModifier) {
		switch(event.getClick()) {
			case NUMBER_KEY: {
				return amount * 10 + event.getHotbarButton() + 1;
			}

			case DROP: {
				return amount / 10;
			}

			case CONTROL_DROP: {
				return 0;
			}

			default: {
				int increment = (event.isRightClick() ? -1 : 1) * (event.isShiftClick() ? shiftModifier : 1);
	
				return amount + increment;
			}
		}
	}
}
