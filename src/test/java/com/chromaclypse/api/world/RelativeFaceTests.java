package com.chromaclypse.api.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.chromaclypse.api.world.RelativeFace.Rotation;

public class RelativeFaceTests {
	private RelativeFace face;
	
	@BeforeEach
	public void setup() {
		face = new RelativeFace(BlockFace.NORTH);
	}
	
	@DisplayName("Left rotation (90 degrees)")
	@Test
	public void turnLeft() {
		assertEquals(BlockFace.WEST, face.toLeft().getFacing());
		assertEquals(BlockFace.SOUTH, face.toLeft().getFacing());
		assertEquals(BlockFace.EAST, face.toLeft().getFacing());
		assertEquals(BlockFace.NORTH, face.toLeft().getFacing());
	}

	@DisplayName("Right rotation (90 degrees)")
	@Test
	public void turnRight() {
		assertEquals(BlockFace.EAST, face.toRight().getFacing());
		assertEquals(BlockFace.SOUTH, face.toRight().getFacing());
		assertEquals(BlockFace.WEST, face.toRight().getFacing());
		assertEquals(BlockFace.NORTH, face.toRight().getFacing());
	}

	@DisplayName("Varying Rotations")
	@ParameterizedTest(name = "Rotate NORTH by {1} is {0}")
	@CsvSource({
		"NORTH, C_ZERO", "NORTH_NORTH_WEST, P_22_5",  "NORTH_WEST, P_45",  "WEST_NORTH_WEST,  P_67_5",
		"WEST,  P_90",   "WEST_SOUTH_WEST,  P_112_5", "SOUTH_WEST, P_135", "SOUTH_SOUTH_WEST, P_157_5",
		"SOUTH, C_180",  "SOUTH_SOUTH_EAST, N_157_5", "SOUTH_EAST, N_135", "EAST_SOUTH_EAST,  N_112_5",
		"EAST,  N_90",   "EAST_NORTH_EAST,  N_67_5",  "NORTH_EAST, N_45",  "NORTH_NORTH_EAST, N_22_5"
	})
	@Order(3)
	public void rotateTest(BlockFace expected, Rotation amount) {
		assertEquals(expected, face.toLeft(amount).getFacing());
	}

	@DisplayName("Static rotation test")
	@Test
	public void staticRotate() {
		assertEquals(BlockFace.EAST, RelativeFace.toRight(BlockFace.NORTH));
		assertEquals(BlockFace.WEST, RelativeFace.toLeft(BlockFace.NORTH));
		assertEquals(BlockFace.WEST, RelativeFace.toRight(BlockFace.NORTH, Rotation.N_90));
		assertEquals(BlockFace.EAST, RelativeFace.toLeft(BlockFace.NORTH, Rotation.N_90));
	}

	@DisplayName("Varying Rotations")
	@ParameterizedTest(name = "Angle between {0} and NORTH is {1}")
	@CsvSource({
		"NORTH, C_ZERO", "NORTH_NORTH_WEST, P_22_5",  "NORTH_WEST, P_45",  "WEST_NORTH_WEST,  P_67_5",
		"WEST,  P_90",   "WEST_SOUTH_WEST,  P_112_5", "SOUTH_WEST, P_135", "SOUTH_SOUTH_WEST, P_157_5",
		"SOUTH, C_180",  "SOUTH_SOUTH_EAST, N_157_5", "SOUTH_EAST, N_135", "EAST_SOUTH_EAST,  N_112_5",
		"EAST,  N_90",   "EAST_NORTH_EAST,  N_67_5",  "NORTH_EAST, N_45",  "NORTH_NORTH_EAST, N_22_5"
	})
	public void getRotationTest(BlockFace face, Rotation expected) {
		assertEquals(expected, RelativeFace.getRotation(face, BlockFace.NORTH));
	}

	@DisplayName("Invalid Rotation Measurement")
	@ParameterizedTest(name = "{0}")
	@CsvSource({
		"UP", "DOWN", "SELF"
	})
	public void invalidRotation(BlockFace face) {
		assertThrows(IllegalArgumentException.class, () -> {
			RelativeFace.getRotation(face, BlockFace.NORTH);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			RelativeFace.getRotation(BlockFace.NORTH, face);
		});
	}

	@DisplayName("Rotation Arightmatic")
	@Test
	public void rotationArithmatic() {
		Rotation r = Rotation.P_90;

		assertEquals(Rotation.P_135, r.add(Rotation.P_45));
		assertEquals(Rotation.N_112_5, r.add(Rotation.P_157_5));
		assertEquals(Rotation.P_45, r.subtract(Rotation.P_45));
		assertEquals(Rotation.N_90, r.subtract(Rotation.C_180));
		assertEquals(Rotation.N_90, r.multiply(7));
		assertEquals(Rotation.P_90, r.multiply(-7));
	}

	@DisplayName("Face Comparisons")
	@Test
	public void faceComparisons() {
		assertEquals(new RelativeFace(BlockFace.WEST), face.toLeft());
		assertNotEquals(new RelativeFace(BlockFace.SOUTH), face.toRight());
		assertNotEquals(face, BlockFace.NORTH);
		assertEquals(face.hashCode(), BlockFace.NORTH.hashCode());
	}
}
