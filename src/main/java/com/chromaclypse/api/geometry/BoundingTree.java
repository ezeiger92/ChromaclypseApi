package com.chromaclypse.api.geometry;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.chromaclypse.api.collision.CollisionData;
import com.chromaclypse.api.collision.ConvexShape;

public class BoundingTree<T extends ConvexShape> implements Iterable<CollisionData<T>> {
	private Node<T> root = null;
	private int nextInsertIndex = Integer.MIN_VALUE;
	private HashMap<Integer, Node<T>> nodes = new HashMap<>();
	private World world;
	
	private static final double expansionFactor = 0.1;
	
	@SuppressWarnings("unchecked")
	private static <T extends ConvexShape> T combine(T left, T right) {
		return (T) left.combinedWith(right);
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends ConvexShape> T copy(T shape) {
		return (T) shape.clone();
	}
	
	private static class Node<T extends ConvexShape> {
		private int index;
		private Node<T> parent;
		private T shape;
		private Object userData;
		
		private Node<T> left = null;
		private Node<T> right = null;
		private int height = 0;
		
		private Node(int index, Node<T> parent, T shape, Object userData) {
			this.index = index;
			this.parent = parent;
			this.shape = shape;
			this.userData = userData;
		}
		
		private void udpate() {
			if(right == null) {
				height = 0;
			}
			else {
				height = Math.max(right.height, left.height) + 1;
				shape = combine(right.shape, left.shape);
			}
		}
		
		@Override
		public String toString() {
			return "h: " + height + ", bounds: " + shape + ", data: " + userData ;
		}
	}
	
	public BoundingTree(World world) {
		this.world = world;
	}
	
	private void assignRefs(Node<T> target, Node<T> to) {
		if(target.parent == null) {
			root = to;
		}
		else if(target.parent.left == target) {
			target.parent.left = to;
		}
		else {
			target.parent.right = to;
		}
	}
	
	private void balanceAndUpdate(Node<T> start) {
		if(start != null) {
			if(start.right == null) {
				start.height = 0;
			}
			else {
				int rh = start.right.height;
				int lh = start.left.height;
				
				if(Math.abs(rh - lh) <= 1) {
					start.height = Math.max(rh, lh) + 1;
					start.shape = combine(start.right.shape, start.left.shape);
				}
				else {
					Node<T> op = start.parent;
					Node<T> pivot = rh >= lh ? start.right : start.left;
					Node<T> small = pivot.right;
					
					if(op == null) {
						op = root;
					}
					
					if(small.height > pivot.left.height) {
						small = pivot.left;
					}
					
					pivot.parent = op.parent;
					op.parent = pivot;
					small.parent = op;
					
					Node<T> temp = op;
					assignRefs(op, pivot);
					assignRefs(pivot, small);
					assignRefs(small, temp);
					
					small.udpate();
					op.udpate();

					small.height = Math.max(rh, lh);
					small.shape = (T) combine(small.right.shape, small.left.shape);
					
					op.height = Math.max(rh, lh);
					op.shape = (T) combine(op.right.shape, op.left.shape);
				}
			}
			
			balanceAndUpdate(start.parent);
		}
	}
	
	private Node<T> store(int index, Node<T> parent, T aabb, Object clientData) {
		Node<T> node = new Node<>(index, parent, aabb, clientData);
		nodes.put(index, node);
		
		return node;
	}
	
	private void doInsertion(int index, Node<T> start, T aabb, Object clientData, int depth) {
		if(start.height == 0) {
			Node<T> fork = new Node<>(index, start.parent, (T) combine(aabb, start.shape), null);
			
			fork.left = start;
			fork.right = store(index, fork, aabb, clientData);
			
			if(start.parent == null) {
				root = fork;
			}
			else if(start.parent.left == start){
				start.parent.left = fork;
			}
			else {
				start.parent.right = fork;
			}
			
			balanceAndUpdate(fork);
		}
		else {
			double totalLeftArea = aabb.combinedWith(start.left.shape).getSurfaceArea()
					+ start.right.shape.getSurfaceArea();
			
			double totalRightArea = aabb.combinedWith(start.right.shape).getSurfaceArea()
					+ start.left.shape.getSurfaceArea();
			
			if(totalLeftArea < totalRightArea) {
				doInsertion(index, start.left, aabb, clientData, ++depth);
			}
			else {
				doInsertion(index, start.right, aabb, clientData, ++depth);
			}
		}
	}
	
	public int insertData(T aabb, Object clientData) {
		int index = nextInsertIndex++;
		insertDataAt(index, aabb, clientData);
		
		return index;
	}
	
	private void insertDataAt(int index, T aabb, Object clientData) {
		T toInsert = copy(aabb);
		toInsert.expandBy(expansionFactor);
		
		if(root == null) {
			root = store(index, null, toInsert, clientData);
		}
		else {
			doInsertion(index, root, aabb, clientData, 0);
		}
	}
	
	public void updateData(int index, T aabb, Object clientData) {
		Node<T> target = nodes.get(index);
		
		if(target != null) {
			if(!target.shape.contains(aabb)) {
				removeData(index);
				insertDataAt(index, aabb, clientData);
			}
			else if(!target.userData.equals(clientData)) {
				target.userData = clientData;
			}
		}
	}
	
	public void removeData(int index) {
		Node<T> target = nodes.get(index);
		
		if(target != null) {
			Node<T> parent = target.parent;
			if(parent == null) {
				root = null;
			}
			else {
				Node<T> grandparent = parent.parent;
				
				Node<T> sibling = parent.left;
				
				if(sibling == target) {
					sibling = parent.right;
				}
				
				if(grandparent == null) {
					root = sibling;
				}
				else {
					if(parent == grandparent.left) {
						grandparent.left = sibling;
					}
					else {
						grandparent.right = sibling;
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
	
	private void findNodes(Function<Node<T>, Boolean> match, List<CollisionData<T>> results) {
		ArrayDeque<Node<T>> toVisit = new ArrayDeque<>();
		toVisit.add(root);
		
		while(!toVisit.isEmpty()) {
			Node<T> node = toVisit.pop();
			if(node == null || !match.apply(node)) {
				continue;
			}
			
			if(node.height == 0) {
				results.add(new CollisionData<>(node.index, node.shape, node.userData));
			}
			else {
				toVisit.add(node.left);
				toVisit.add(node.right);
			}
		}
	}
	
	public List<CollisionData<T>> queryArea(T bounds) {
		List<CollisionData<T>> results = new ArrayList<>();
		findNodes(n -> n.shape.intersects(bounds), results);
		
		return results;
		
	}
	
	public List<CollisionData<T>> queryPoint(Vector point) {
		List<CollisionData<T>> results = new ArrayList<>();
		findNodes(n -> n.shape.contains(point), results);
		
		return results;
		
	}
	
	public List<CollisionData<T>> queryPoint(Location point) {
		if(point.getWorld() == world) {
			return queryPoint(point.toVector());
		}
		
		return new ArrayList<>();
	}
	
	public World getWorld() {
		return world;
	}
	
	public class LeafIterator implements Iterator<CollisionData<T>> {
		private Iterator<Node<T>> innerIt;
		
		public LeafIterator() {
			innerIt = nodes.values().iterator();
		}
		
		@Override
		public boolean hasNext() {
			return innerIt.hasNext();
		}

		@Override
		public CollisionData<T> next() {
			Node<T> node = innerIt.next();
			return new CollisionData<>(node.index, node.shape, node.userData);
		}
		
	}

	@Override
	public Iterator<CollisionData<T>> iterator() {
		return new LeafIterator();
	}
}
