package com.royrodriguez.vb.utils;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;

import sun.misc.Cleaner;

public class Quadtree {
	
	// El numero maximo que permite el nodo antes que divide 
	private final int MAX_OBJECTS = 10;

	// Es los maximos sub nodos que se pueden tener
	private final int MAX_LEVEL = 5;
	
	private int level;
	
	// Representa el espacio que el nodo ocuoa
	private Rectangle bounds;
	
	private ArrayList<Entity> entitys;
	
	// Los cuatro sub nodos.
	private Quadtree[] nodes;
	
	public Quadtree(int level, Rectangle bounds) {
		this.level = level;
		this.bounds = bounds;
		
		entitys = new ArrayList<Entity>();
		nodes = new Quadtree[4];
	}
	
	public void clear() {
		// Limpia los objetos del nodo y sus subnodos
		entitys.clear();
		
		for(int i = 0; i < nodes.length; i++) {
			if(nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
		
	}
	
	private void split() {
		int subWidth = (int) (bounds.getWidth() / 2);
		int subHeight = (int) (bounds.getHeight() / 2);
		
		int horizontalMidPoint = (int) (bounds.getX() + subWidth);
		int verticalMidPoint = (int) (bounds.getY() + subHeight);
		
		nodes[0] = new Quadtree(level + 1, new Rectangle(horizontalMidPoint, verticalMidPoint, subWidth, subHeight));
		nodes[1] = new Quadtree(level + 1, new Rectangle(horizontalMidPoint - subWidth, verticalMidPoint, subWidth, subHeight));
		nodes[2] = new Quadtree(level + 1, new Rectangle(horizontalMidPoint - subWidth, verticalMidPoint - subHeight, subWidth, subHeight));
		nodes[3] = new Quadtree(level + 1, new Rectangle(horizontalMidPoint,  verticalMidPoint - subHeight, subWidth, subHeight));
	}
	
	// Los 
	private int getIndex(Rectangle o) {
		// 1  0
		// 3  4
		int index = -1;
		int horizontalMidPoint = (int) (bounds.getX() + bounds.getWidth() / 2);
		int verticalMidPoint = (int) (bounds.getY() + bounds.getHeight());
		
		
		boolean topQuadrant = o.y > verticalMidPoint;
		boolean bottomQuadrant = o.y + o.height < verticalMidPoint;
		boolean rightQuadrant = o.x > horizontalMidPoint;
		boolean leftQuadrant = o.x + o.width < horizontalMidPoint;
		
		if(rightQuadrant) {
			if(topQuadrant)
				index = 0;
			else if(bottomQuadrant)
				index = 4;

	 	} else if(leftQuadrant){
	 		if(topQuadrant)
				index = 1;
			else if(bottomQuadrant)
				index = 3;
	 	}
		
		return index;

	}
	
	private void insert(Rectangle r) {
		
	}
	
}
