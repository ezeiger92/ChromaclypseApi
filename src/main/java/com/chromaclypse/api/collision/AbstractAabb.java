package com.chromaclypse.api.collision;

import org.bukkit.util.Vector;

public abstract class AbstractAabb implements Cloneable {

	public abstract boolean contains(Vector location);
	public abstract boolean contains(AbstractAabb area);
	public abstract boolean intersects(AbstractAabb area);
	
	public abstract AbstractAabb combinedWith(AbstractAabb other);
	
	public abstract Vector getMin();
	public abstract Vector getMax();
	
	public abstract double getSurfaceArea();
	
	public abstract void expandBy(double factor);

	@Override
	public AbstractAabb clone() {
		try {
			return (AbstractAabb) super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
}
