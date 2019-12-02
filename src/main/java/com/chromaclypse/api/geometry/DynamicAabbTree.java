package com.chromaclypse.api.geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.chromaclypse.api.collision.AbstractAabb;
import com.chromaclypse.api.collision.CollisionData;

public class DynamicAabbTree implements Iterable<CollisionData> {
	private Node root = null;
	private int nextInsertIndex = Integer.MIN_VALUE;
	private HashMap<Integer, Node> nodes = new HashMap<>();
	private World mWorld;
	
	private static final double expansionFactor = 0.1;
	
	private static class Node {
		private int mIndex;
		private Node mParent;
		private AbstractAabb mAabb;
		private Object mUserData;
		
		private Node mLeft = null;
		private Node mRight = null;
		private int mHeight = 0;
		
		private Node(int index, Node parent, AbstractAabb aabb, Object userData) {
			mIndex = index;
			mParent = parent;
			mAabb = aabb;
			mUserData = userData;
		}
		
		private void udpate() {
			if(mRight == null) {
				mHeight = 0;
			}
			else {
				mHeight = Math.max(mRight.mHeight, mLeft.mHeight) + 1;
				mAabb = mRight.mAabb.combinedWith(mLeft.mAabb);
			}
		}
		
		@Override
		public String toString() {
			return "h: " + mHeight + ", aabb: [" + mAabb.getMin().getX() + ".." + mAabb.getMax().getX() + "][" 
					 + mAabb.getMin().getY() + ".." + mAabb.getMax().getY() + "][" 
					 + mAabb.getMin().getZ() + ".." + mAabb.getMax().getZ() + "], data: " + mUserData ;
		}
	}
	
	public DynamicAabbTree(World world) {
		mWorld = world;
	}
	
	private void assignRefs(Node target, Node to) {
		if(target.mParent == null) {
			root = to;
		}
		else if(target.mParent.mLeft == target) {
			target.mParent.mLeft = to;
		}
		else {
			target.mParent.mRight = to;
		}
	}
	
	private void balanceAndUpdate(Node start) {
		if(start != null) {
			if(start.mRight == null) {
				start.mHeight = 0;
			}
			else {
				int rh = start.mRight.mHeight;
				int lh = start.mLeft.mHeight;
				
				if(Math.abs(rh - lh) <= 1) {
					start.mHeight = Math.max(rh, lh) + 1;
					start.mAabb = start.mRight.mAabb.combinedWith(start.mLeft.mAabb);
				}
				else {
					Node op = start.mParent;
					Node pivot = rh >= lh ? start.mRight : start.mLeft;
					Node small = pivot.mRight;
					
					if(op == null) {
						op = root;
					}
					
					if(small.mHeight > pivot.mLeft.mHeight) {
						small = pivot.mLeft;
					}
					
					pivot.mParent = op.mParent;
					op.mParent = pivot;
					small.mParent = op;
					
					Node temp = op;
					assignRefs(op, pivot);
					assignRefs(pivot, small);
					assignRefs(small, temp);
					
					small.udpate();
					op.udpate();

					small.mHeight = Math.max(rh, lh);
					small.mAabb = small.mRight.mAabb.combinedWith(small.mLeft.mAabb);
					
					op.mHeight = Math.max(rh, lh);
					op.mAabb = op.mRight.mAabb.combinedWith(op.mLeft.mAabb);
				}
			}
			
			balanceAndUpdate(start.mParent);
		}
	}
	
	private Node store(int index, Node parent, AbstractAabb aabb, Object clientData) {
		Node node = new Node(index, parent, aabb, clientData);
		nodes.put(index, node);
		
		return node;
	}
	
	private void doInsertion(int index, Node start, AbstractAabb aabb, Object clientData, int depth) {
		if(start.mHeight == 0) {
			Node fork = new Node(index, start.mParent, aabb.combinedWith(start.mAabb), null);
			
			fork.mLeft = start;
			fork.mRight = store(index, fork, aabb, clientData);
			
			if(start.mParent == null) {
				root = fork;
			}
			else if(start.mParent.mLeft == start){
				start.mParent.mLeft = fork;
			}
			else {
				start.mParent.mRight = fork;
			}
			
			balanceAndUpdate(fork);
		}
		else {
			double totalLeftArea = aabb.combinedWith(start.mLeft.mAabb).getSurfaceArea()
					+ start.mRight.mAabb.getSurfaceArea();
			
			double totalRightArea = aabb.combinedWith(start.mRight.mAabb).getSurfaceArea()
					+ start.mLeft.mAabb.getSurfaceArea();
			
			if(totalLeftArea < totalRightArea) {
				doInsertion(index, start.mLeft, aabb, clientData, ++depth);
			}
			else {
				doInsertion(index, start.mRight, aabb, clientData, ++depth);
			}
		}
	}
	
	public int insertData(AbstractAabb aabb, Object clientData) {
		int index = nextInsertIndex++;
		insertDataAt(index, aabb, clientData);
		
		return index;
	}
	
	private void insertDataAt(int index, AbstractAabb aabb, Object clientData) {
		AbstractAabb toInsert = aabb.clone();
		toInsert.expandBy(expansionFactor);
		
		if(root == null) {
			root = store(index, null, toInsert, clientData);
		}
		else {
			doInsertion(index, root, aabb, clientData, 0);
		}
	}
	
	public void updateData(int index, AbstractAabb aabb, Object clientData) {
		Node target = nodes.get(index);
		
		if(target != null) {
			if(!target.mAabb.contains(aabb)) {
				removeData(index);
				insertDataAt(index, aabb, clientData);
			}
			else if(!target.mUserData.equals(clientData)) {
				target.mUserData = clientData;
			}
		}
	}
	
	public void removeData(int index) {
		Node target = nodes.get(index);
		
		if(target != null) {
			Node parent = target.mParent;
			if(parent == null) {
				root = null;
			}
			else {
				Node grandparent = parent.mParent;
				
				Node sibling = parent.mLeft;
				
				if(sibling == target) {
					sibling = parent.mRight;
				}
				
				if(grandparent == null) {
					root = sibling;
				}
				else {
					if(parent == grandparent.mLeft) {
						grandparent.mLeft = sibling;
					}
					else {
						grandparent.mRight = sibling;
					}
				}
				
				balanceAndUpdate(sibling);
			}
		}
	}
	
	public boolean hasData(int index) {
		return nodes.containsKey(index);
	}
	
	public Object getData(int index) {
		return nodes.get(index);
	}
	
	private void recursePoint(Node start, Vector point, List<CollisionData> results) {

		if(start != null) {
			if(start.mAabb.contains(point)) {
				if(start.mHeight == 0) {
					results.add(new CollisionData(start.mIndex, start.mAabb, start.mUserData));
				}
				else {
					recursePoint(start.mLeft, point, results);
					recursePoint(start.mRight, point, results);
				}
			}
			
		}
	}
	
	private void recurseArea(Node start, AbstractAabb bounds, List<CollisionData> results) {

		if(start != null) {
			if(start.mAabb.intersects(bounds)) {
				if(start.mHeight == 0) {
					results.add(new CollisionData(start.mIndex, start.mAabb, start.mUserData));
				}
				else {
					recurseArea(start.mLeft, bounds, results);
					recurseArea(start.mRight, bounds, results);
				}
			}
			
		}
	}
	
	public List<CollisionData> queryArea(AbstractAabb bounds) {
		List<CollisionData> results = new ArrayList<>();
		recurseArea(root, bounds, results);
		
		return results;
		
	}
	
	public List<CollisionData> queryPoint(Vector point) {
		List<CollisionData> results = new ArrayList<>();
		recursePoint(root, point, results);
		
		return results;
		
	}
	
	public List<CollisionData> queryPoint(Location point) {
		List<CollisionData> results = new ArrayList<>();
		
		if(point.getWorld() == mWorld) {
			recursePoint(root, point.toVector(), results);
		}
		
		return results;
	}
	
	public World getWorld() {
		return mWorld;
	}
	
	public class LeafIterator implements Iterator<CollisionData> {
		private Iterator<Node> innerIt;
		
		public LeafIterator() {
			innerIt = nodes.values().iterator();
		}
		
		@Override
		public boolean hasNext() {
			return innerIt.hasNext();
		}

		@Override
		public CollisionData next() {
			Node node = innerIt.next();
			CollisionData result = new CollisionData(node.mIndex, node.mAabb, node.mUserData);
			
			return result;
		}
		
	}

	@Override
	public Iterator<CollisionData> iterator() {
		return new LeafIterator();
	}
}
