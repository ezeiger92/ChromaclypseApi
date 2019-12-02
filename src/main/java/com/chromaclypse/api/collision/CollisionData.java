package com.chromaclypse.api.collision;

public class CollisionData {
	public final int index;
	public final AbstractAabb bounds;
	public final Object data;
	
	public CollisionData(int index, AbstractAabb bounds, Object data) {
		this.index = index;
		this.bounds = bounds;
		this.data = data;
	}
}
