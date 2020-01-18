package com.chromaclypse.api.collision;

import org.bukkit.util.Vector;

public abstract class AbstractAabb extends ConvexShape implements Cloneable {
	public abstract Vector getMin();
	public abstract Vector getMax();
	public abstract Vector getCenter();

	@Override
	public AbstractAabb clone() {
		return (AbstractAabb) super.clone();
	}
}
