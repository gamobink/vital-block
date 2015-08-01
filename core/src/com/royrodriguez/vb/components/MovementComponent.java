package com.royrodriguez.vb.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent extends Component {
	public Vector2 vel = new Vector2();
	public Vector2 accel = new Vector2();
}
