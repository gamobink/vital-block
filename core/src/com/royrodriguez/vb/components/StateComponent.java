package com.royrodriguez.vb.components;

import com.badlogic.ashley.core.Component;

public class StateComponent extends Component {
	
	public int currentState = 0;
	
	public float time = 0;
	
	public void set(int state) {
		if(currentState == state) return;

		time =  0;
		currentState = state;
	}

}
