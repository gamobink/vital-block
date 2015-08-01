package com.royrodriguez.vb.components;

import com.badlogic.ashley.core.Component;

public class MapCollisionComponent extends Component {
	public static final String SOLID_LAYER = "solid";
	public static final String BACK_LAYER = "back";
	public static final String FLIP_LAYER = "flip";
	public static final String VEL_LAYER = "vel";
	public static final String SKY_LAYER = "sky";
	
	public enum CellType {
		SOLID, FLIP, NONE, VEL;
	};

	public boolean grounded = false;
	
	public CellType bottomCell = CellType.NONE;
	
}
