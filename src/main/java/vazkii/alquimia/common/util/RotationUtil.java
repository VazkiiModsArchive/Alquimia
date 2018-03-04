package vazkii.alquimia.common.util;

import net.minecraft.util.Rotation;

public class RotationUtil {

	public static int x(Rotation rot, int x, int z) {
		switch(rot) {
		case NONE: return x;
		case CLOCKWISE_180: return -x;
		case CLOCKWISE_90: return z;
		default: return -z;
		}
	}
	
	public static int z(Rotation rot, int x, int z) {
		switch(rot) {
		case NONE: return z;
		case CLOCKWISE_180: return -z;
		case CLOCKWISE_90: return x;
		default: return -x;
		}
	}

}
