package com.chromaclypse.api.collision;

public class CollisionData<T extends ConvexShape> {
	public final int index;
	public final T bounds;
	public final Object data;
	
	public CollisionData(int index, T bounds, Object data) {
		this.index = index;
		this.bounds = bounds;
		this.data = data;
	}
}
