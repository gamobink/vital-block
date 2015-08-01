package com.royrodriguez.vb.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent extends Component {
	public Vector2 pos = new Vector2();
	public Vector2 scale = new Vector2(1, 1);
	public float angle = 0;
}
