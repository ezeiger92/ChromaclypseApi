package com.chromaclypse.api.collision;

import org.bukkit.util.Vector;

public abstract class ConvexShape implements Cloneable {
	public abstract boolean contains(Vector location);
	public abstract boolean contains(ConvexShape other);
	public abstract boolean intersects(ConvexShape other);
	public abstract double getSurfaceArea();
	
	public abstract ConvexShape combinedWith(ConvexShape other);
	public abstract void expandBy(double factor);
	
	public abstract String toString();

	@Override
	public ConvexShape clone() {
		try {
			return (ConvexShape) super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
}
