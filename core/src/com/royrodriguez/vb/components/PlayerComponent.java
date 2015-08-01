package com.royrodriguez.vb.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent extends Component {
	
	public static int IDLE_STATE = 0;
	public static int WALK_STATE = 1;
	public static int JUMP_STATE = 2;
	public static int MAX_VEL = 9;
	
	public static float jumpVel = 11;
	
	public static float maxVel = 12;

	public static float accelX = 3;
	
	public boolean top = true;
	
}
