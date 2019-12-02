package com.chromaclypse.api.entity;

import org.bukkit.entity.EntityType;

@Deprecated
public enum EntityOther {
	CHARGED_CREEPER(EntityType.CREEPER, "powered:1"),
	ZOMBIE_FARMER_VILLAGER(    EntityType.ZOMBIE_VILLAGER, "Profession:0"),
	ZOMBIE_LIBRARIAN_VILLAGER( EntityType.ZOMBIE_VILLAGER, "Profession:1"),
	ZOMBIE_PRIEST_VILLAGER(    EntityType.ZOMBIE_VILLAGER, "Profession:2"),
	ZOMBIE_BLACKSMITH_VILLAGER(EntityType.ZOMBIE_VILLAGER, "Profession:3"),
	ZOMBIE_BUTCHER_VILLAGER(   EntityType.ZOMBIE_VILLAGER, "Profession:4"),
	ZOMBIE_NITWIT_VILLAGER(    EntityType.ZOMBIE_VILLAGER, "Profession:5"),
	FARMER_VILLAGER(       EntityType.VILLAGER, "Profession:0,Career:1"),
	FISHERMAN_VILLAGER(    EntityType.VILLAGER, "Profession:0,Career:2"),
	SHEPHERD_VILLAGER(     EntityType.VILLAGER, "Profession:0,Career:3"),
	FLETCHER_VILLAGER(     EntityType.VILLAGER, "Profession:0,Career:4"),
	LIBRARIAN_VILLAGER(    EntityType.VILLAGER, "Profession:1,Career:1"),
	CARTOGRAPHER_VILLAGER( EntityType.VILLAGER, "Profession:1,Career:2"),
	CLERIC_VILLAGER(       EntityType.VILLAGER, "Profession:2,Career:1"),
	ARMORER_VILLAGER(      EntityType.VILLAGER, "Profession:3,Career:1"),
	WEAPON_SMITH_VILLAGER( EntityType.VILLAGER, "Profession:3,Career:2"),
	TOOL_SMITH_VILLAGER(   EntityType.VILLAGER, "Profession:3,Career:3"),
	BUTCHER_VILLAGER(      EntityType.VILLAGER, "Profession:4,Career:1"),
	LEATHERWORKER_VILLAGER(EntityType.VILLAGER, "Profession:4,Career:2"),
	NITWIT_VILLAGER(       EntityType.VILLAGER, "Profession:5,Career:1"),
	GENERIC_FARMER_VILLAGER(    EntityType.VILLAGER, "Profession:0"),
	GENERIC_LIBRARIAN_VILLAGER( EntityType.VILLAGER, "Profession:1"),
	GENERIC_PRIEST_VILLAGER(    EntityType.VILLAGER, "Profession:2"),
	GENERIC_BLACKSMITH_VILLAGER(EntityType.VILLAGER, "Profession:3"),
	GENERIC_BUTCHER_VILLAGER(   EntityType.VILLAGER, "Profession:4"),
	GENERIC_NITWIT_VILLAGER(    EntityType.VILLAGER, "Profession:5"),
	;
	private EntityType base;
	private String properties;

	EntityOther(EntityType base, String properties) {
		this.base = base;
		this.properties = properties;
	}
	
	public EntityTag getItemTag() {
		return new EntityTag(base, properties);
	}
	
	public EntityType getEntity() {
		return base;
	}
	
	public String getProperties() {
		return properties;
	}
}
