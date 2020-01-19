package com.chromaclypse.api.collision;

import org.bukkit.util.Vector;

public class Circle extends ConvexShape {
	private Vector center;
	private double radius;
	
	public Circle(Vector center, double radius) {
		this.center = center.clone();
		this.radius = radius;
	}
	
	@Override
	public boolean contains(Vector location) {
		return center.distanceSquared(location) <= radius * radius;
	}

	@Override
	public boolean contains(ConvexShape other) {
		if(other instanceof Circle) {
			Circle otherCircle = (Circle) other;
			
			double difference = radius * radius - otherCircle.radius * otherCircle.radius;
			
			return difference < 0 ||
					center.distanceSquared(otherCircle.center) < difference;
		}
		return false;
	}

	@Override
	public boolean intersects(ConvexShape other) {
		if(other instanceof Circle) {
			Circle otherCircle = (Circle) other;
			
			double radius = otherCircle.radius + this.radius;
			
			return center.distanceSquared(otherCircle.center) < radius * radius;
		}
		return false;
	}

	@Override
	public double getBalancingHeuristic() {
		return radius * radius;
	}

	@Override
	public ConvexShape combinedWith(ConvexShape other) {
		if(other instanceof Circle) {
			Circle otherCircle = (Circle) other;
			
			Vector direction = otherCircle.center.clone().subtract(center).normalize();
			Vector side = center.clone().subtract(direction.clone().multiply(radius));
			Vector otherSide = otherCircle.center.clone().add(direction.multiply(otherCircle.radius));
			
			center = side.midpoint(otherSide);
			radius = side.length() / 2;
		}
		
		return this;
	}

	@Override
	public void expandBy(double factor) {
	}

	@Override
	public String toString() {
		return "";
	}

}
