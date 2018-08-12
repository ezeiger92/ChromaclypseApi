package com.chromaclypse.api.collision;

import org.bukkit.util.Vector;

public class BlockAabb extends AbstractAabb {

	public static class Point implements Cloneable {
		public int mX;
		public int mY;
		public int mZ;
		
		public Point() {
			mX = 0;
			mY = 0;
			mZ = 0;
		}
		
		public Point(int x, int y, int z) {
			mX = x;
			mY = y;
			mZ = z;
		}
		
		public Point(Vector vec) {
			mX = vec.getBlockX();
			mY = vec.getBlockY();
			mZ = vec.getBlockZ();
		}
		
		public Vector makeVector() {
			return new Vector(mX, mY, mZ);
		}
		
		@Override
		public Point clone() {
			try {
				return (Point) super.clone();
			}
			catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}
	}
	
	private Point mMin;
	private Point mMax;
	
	public BlockAabb(Vector min, Vector max) {
		mMin = new Point(min);
		mMax = new Point(max);
	}
	
	public BlockAabb(int lx, int ly, int lz, int ux, int uy, int uz) {
		mMin = new Point(lx, ly, lz);
		mMax = new Point(ux, uy, uz);
	}

	@Override
	public boolean contains(Vector location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		
		return !(mMin.mX > x || mMax.mX < x ||
				mMin.mZ > z || mMax.mZ < z ||
				mMin.mY > y || mMax.mY < y);
	}
	
	private BlockAabb fromAbstractAabb(AbstractAabb aabb) {
		BlockAabb result;
		
		if(aabb instanceof BlockAabb) {
			result = ((BlockAabb)aabb).clone();
		}
		else {
			result = new BlockAabb(aabb.getMin(), aabb.getMax());
		}
		
		return result;
	}
	
	@Override
	public boolean contains(AbstractAabb area) {
		BlockAabb other = fromAbstractAabb(area);
		Point oMin = other.mMin;
		Point oMax = other.mMax;
		
		return !(mMin.mX > oMin.mX || mMax.mX < oMax.mX ||
				mMin.mZ > oMin.mZ || mMax.mZ < oMax.mZ ||
				mMin.mY > oMin.mY || mMax.mY < oMax.mY);
	}

	@Override
	public boolean intersects(AbstractAabb area) {
		BlockAabb other = fromAbstractAabb(area);
		Point oMin = other.mMin;
		Point oMax = other.mMax;
		
		return !(mMin.mX > oMax.mX || mMax.mX < oMin.mX ||
				mMin.mZ > oMax.mZ || mMax.mZ < oMin.mZ ||
				mMin.mY > oMax.mY || mMax.mY < oMin.mY);
	}

	@Override
	public AbstractAabb combinedWith(AbstractAabb aabb) {
		BlockAabb other = fromAbstractAabb(aabb);
		Point oMin = other.mMin;
		Point oMax = other.mMax;
		
		BlockAabb result = clone();

		result.mMin.mX = Math.min(mMin.mX, oMin.mX);
		result.mMin.mY = Math.min(mMin.mY, oMin.mY);
		result.mMin.mZ = Math.min(mMin.mZ, oMin.mZ);

		result.mMax.mX = Math.max(mMax.mX, oMax.mX);
		result.mMax.mY = Math.max(mMax.mY, oMax.mY);
		result.mMax.mZ = Math.max(mMax.mZ, oMax.mZ);
		
		return result;
	}

	@Override
	public Vector getMin() {
		return mMin.makeVector();
	}

	@Override
	public Vector getMax() {
		return mMax.makeVector();
	}
	
	@Override
	public double getSurfaceArea() {
		int dx = mMax.mX - mMin.mX;
		int dy = mMax.mY - mMin.mY;
		int dz = mMax.mZ - mMin.mZ;
		
		return dx * dy * 2 + dx * dz * 2 + dy * dz * 2;
	}

	// Exact block locations require no expansion
	@Override
	public void expandBy(double factor) {
	}
	
	@Override
	public BlockAabb clone() {
		BlockAabb aabb = (BlockAabb) super.clone();
		aabb.mMin = mMin.clone();
		aabb.mMax = mMax.clone();
		
		return aabb;
	}
}
