package com.chromaclypse.api.world;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.bukkit.block.BlockFace;

public class RelativeFace {
	private static final Rotation defaultRotation = Rotation.P_90;
	private BlockFace facing;
	
	public RelativeFace(BlockFace facing) {
		this.facing = facing;
	}
	
	public BlockFace getFacing() {
		return facing;
	}
	
	public RelativeFace toLeft() {
		return toLeft(defaultRotation);
	}
	
	public RelativeFace toLeft(Rotation amount) {
		facing = toLeft(facing, amount);
		return this;
	}
	
	public RelativeFace toRight() {
		return toRight(defaultRotation);
	}
	
	public RelativeFace toRight(Rotation amount) {
		facing = toRight(facing, amount);
		return this;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof RelativeFace && facing == ((RelativeFace) other).facing;
	}
	
	@Override
	public int hashCode() {
		return facing.hashCode();
	}
	
	public static BlockFace toLeft(BlockFace facing) {
		return toLeft(facing, defaultRotation);
	}
	
	public static BlockFace toLeft(BlockFace facing, Rotation amount) {
		return rotate(facing, amount.inverse());
	}
	
	public static BlockFace toRight(BlockFace facing) {
		return toRight(facing, defaultRotation);
	}
	
	public static BlockFace toRight(BlockFace facing, Rotation amount) {
		return rotate(facing, amount);
	}
	
	public static Rotation getRotation(BlockFace from, BlockFace to) {
		assertRotatable(from);
		assertRotatable(to);
		
		Rotation[] rotations = Rotation.values();
		int length = rotations.length;
		
		return rotations[(length + faceOrdering.get(to) - faceOrdering.get(from)) % length];
	}
	
	private static BlockFace rotate(BlockFace facing, Rotation amount) {
		assertRotatable(facing);
		
		return orderedFacings.get((faceOrdering.get(facing) + amount.ordinal()) % orderedFacings.size());
	}
	
	private static void assertRotatable(BlockFace facing) {
		switch(facing) {
			case UP:
			case DOWN:
			case SELF:
				throw new IllegalArgumentException("BlockFace." + facing.name() + " is not rotatable");

			default:
				break;
		}
	}
	
	private static final List<BlockFace> orderedFacings;
	private static final Map<BlockFace, Integer> faceOrdering;
	
	static {
		BlockFace[] ordering = {
			BlockFace.NORTH,
			BlockFace.NORTH_NORTH_EAST,
			BlockFace.NORTH_EAST,
			BlockFace.EAST_NORTH_EAST,
			BlockFace.EAST,
			BlockFace.EAST_SOUTH_EAST,
			BlockFace.SOUTH_EAST,
			BlockFace.SOUTH_SOUTH_EAST,
			BlockFace.SOUTH,
			BlockFace.SOUTH_SOUTH_WEST,
			BlockFace.SOUTH_WEST,
			BlockFace.WEST_SOUTH_WEST,
			BlockFace.WEST,
			BlockFace.WEST_NORTH_WEST,
			BlockFace.NORTH_WEST,
			BlockFace.NORTH_NORTH_WEST,
		};
		
		EnumMap<BlockFace, Integer> mapping = new EnumMap<>(BlockFace.class);
		
		int length = ordering.length;
		for(int i = 0; i < length; ++i) {
			mapping.put(ordering[i], i);
		}
		
		faceOrdering = Collections.unmodifiableMap(mapping);
		orderedFacings = Collections.unmodifiableList(Arrays.asList(ordering));
	}
	
	public static enum Rotation {
		C_ZERO,
		P_22_5,
		P_45,
		P_67_5,
		P_90,
		P_112_5,
		P_135,
		P_157_5,
		C_180,
		N_157_5,
		N_135,
		N_112_5,
		N_90,
		N_67_5,
		N_45,
		N_22_5,
		;
		
		public Rotation inverse() {
			Rotation[] rotations = values();
			int length = rotations.length;
			
			return rotations[(length - ordinal()) % length];
		}
		
		public Rotation add(Rotation other) {
			Rotation[] rotations = values();
			
			return rotations[(ordinal() + other.ordinal()) % rotations.length];
		}
		
		public Rotation subtract(Rotation other) {
			Rotation[] rotations = values();
			
			return rotations[(ordinal() - other.ordinal() + rotations.length) % rotations.length];
		}
		
		public Rotation multiply(int times) {
			Rotation[] rotations = values();
			int length = rotations.length;
			
			if(times < 0)
				return rotations[((length - ordinal()) * -times) % length];
			
			return rotations[(ordinal() * times) % rotations.length];
		}
	}
}
