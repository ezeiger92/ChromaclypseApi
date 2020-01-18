package com.chromaclypse.api.geometry;

import org.bukkit.World;

import com.chromaclypse.api.collision.AbstractAabb;

public class DynamicAabbTree extends BoundingTree<AbstractAabb> {

	public DynamicAabbTree(World world) {
		super(world);
	}
}
