package com.chromaclypse.api.collision;

import org.bukkit.util.Vector;

public class Aabb extends AbstractAabb {

	private Vector mMin;
	private Vector mMax;
	
	public Aabb(Vector min, Vector max, double expansionEpsilon) {
		mMin = min.clone();
		mMax = max.clone();
	}
	
	public Aabb(Vector min, Vector max) {
		this(min, max, Vector.getEpsilon());
	}
	
	@Override
	public boolean contains(Vector location) {
		return !(mMin.getX() > location.getX() || mMax.getX() < location.getX() ||
				mMin.getZ() > location.getZ() || mMax.getZ() < location.getZ() ||
				mMin.getY() > location.getY() || mMax.getY() < location.getY());
	}
	
	@Override
	public boolean contains(ConvexShape shape) {
		AbstractAabb area = (AbstractAabb) shape;
		Vector oMin = area.getMin();
		Vector oMax = area.getMax();
		
		return !(mMin.getX() > oMin.getX() || mMax.getX() < oMax.getX() ||
				mMin.getZ() > oMin.getZ() || mMax.getZ() < oMax.getZ() ||
				mMin.getY() > oMin.getY() || mMax.getY() < oMax.getY());
	}

	@Override
	public boolean intersects(ConvexShape shape) {
		AbstractAabb area = (AbstractAabb) shape;
		Vector oMin = area.getMin();
		Vector oMax = area.getMax();
		
		return !(mMin.getX() > oMax.getX() || mMax.getX() < oMin.getX() ||
				mMin.getZ() > oMax.getZ() || mMax.getZ() < oMin.getZ() ||
				mMin.getY() > oMax.getY() || mMax.getY() < oMin.getY());
	}

	@Override
	public AbstractAabb combinedWith(ConvexShape shape) {
		AbstractAabb other = (AbstractAabb) shape;
		Vector oMin = other.getMin();
		Vector oMax = other.getMax();
		
		Aabb result = clone();

		result.mMin.setX(Math.min(mMin.getX(), oMin.getX()));
		result.mMin.setY(Math.min(mMin.getY(), oMin.getY()));
		result.mMin.setZ(Math.min(mMin.getZ(), oMin.getZ()));

		result.mMax.setX(Math.max(mMax.getX(), oMax.getX()));
		result.mMax.setY(Math.max(mMax.getY(), oMax.getY()));
		result.mMax.setZ(Math.max(mMax.getZ(), oMax.getZ()));
		
		return result;
	}

	@Override
	public Vector getMin() {
		return mMin;
	}
	
	@Override
	public Vector getMax() {
		return mMax;
	}
	
	@Override
	public Vector getCenter() {
		return mMin.getMidpoint(mMax);
	}
	
	@Override
	public double getSurfaceArea() {
		double dx = mMax.getX() - mMin.getX();
		double dy = mMax.getY() - mMin.getY();
		double dz = mMax.getZ() - mMin.getZ();
		
		return dx * dy * 2 + dx * dz * 2 + dy * dz * 2;
	}

	@Override
	public void expandBy(double factor) {
		Vector extents = mMax.clone().subtract(mMin).multiply(factor);
		
		mMin.subtract(extents);
		mMax.add(extents);
	}
	
	@Override
	public Aabb clone() {
		Aabb aabb = (Aabb) super.clone();
		aabb.mMin = mMin.clone();
		aabb.mMax = mMax.clone();
		
		return aabb;
	}
	
	@Override
	public String toString() {
		return "";
	}
}
