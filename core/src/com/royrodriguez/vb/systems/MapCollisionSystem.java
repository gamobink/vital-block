package com.royrodriguez.vb.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.royrodriguez.vb.components.BoundsComponent;
import com.royrodriguez.vb.components.MapCollisionComponent;
import com.royrodriguez.vb.components.MapCollisionComponent.CellType;
import com.royrodriguez.vb.utils.RectPool;
import com.royrodriguez.vb.components.MovementComponent;
import com.royrodriguez.vb.components.TextureComponent;
import com.royrodriguez.vb.components.TransformComponent;

public class MapCollisionSystem extends IteratingSystem {

	private Map map;

	private Array<Rectangle> tiles;
	
	private ComponentMapper<MapCollisionComponent> mapCollisionMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<BoundsComponent> boundsMapper;
	private ComponentMapper<MovementComponent> movementMapper;
	private ComponentMapper<TextureComponent> textureMapper;
	
	public MapCollisionSystem(int priority) {
		super(Family.all(MapCollisionComponent.class, TransformComponent.class).get(), priority);

		mapCollisionMapper = ComponentMapper.getFor(MapCollisionComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		boundsMapper = ComponentMapper.getFor(BoundsComponent.class);
		movementMapper = ComponentMapper.getFor(MovementComponent.class);
		textureMapper = ComponentMapper.getFor(TextureComponent.class);
		
		tiles = new Array<Rectangle>();
	}
	
	@Override
	public void update(float deltaTime) {
		if(map == null) return;

		super.update(deltaTime);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Rectangle entityRect = RectPool.getInstance().obtain();

		TransformComponent transformComp = transformMapper.get(entity);
		MapCollisionComponent mapCollisionComponent = mapCollisionMapper.get(entity);
		MovementComponent movementComp = movementMapper.get(entity);
		BoundsComponent boundsComp = boundsMapper.get(entity);
		
		if(deltaTime == 0) deltaTime = 1.0f / 60.0f;
		
		// The body does not have velocity
		entityRect.set(transformComp.pos.x, transformComp.pos.y, boundsComp.width, boundsComp.height);
		
		int starX, startY, endX, endY;
		
		// X AXIS
        if(movementComp.vel.x > 0)
            starX = endX = (int) (transformComp.pos.x + boundsComp.width + movementComp.vel.x * deltaTime);
        else
            starX = endX = (int) (transformComp.pos.x + movementComp.vel.x * deltaTime);
        startY = (int) (transformComp.pos.y);
        endY = (int) (transformComp.pos.y + boundsComp.height);

        getTiles(starX, startY, endX, endY, tiles);
        
        entityRect.x += movementComp.vel.x * deltaTime;
        for(Rectangle tile : tiles) {
            if(entityRect.overlaps(tile)) {
            	 if(movementComp.vel.x > 0)
                     transformComp.pos.x = tile.x - boundsComp.width;
            	 else
                     transformComp.pos.x = tile.x + tile.width;

            	 movementComp.vel.x = 0;

                break;
            }
        }
        
        // Y AXIS
        entityRect.x = transformComp.pos.x;
		
        if(movementComp.vel.y > 0)
            startY = endY = (int) (transformComp.pos.y + boundsComp.height + movementComp.vel.y * deltaTime);
        else
            startY = endY = (int) (transformComp.pos.y + movementComp.vel.y * deltaTime);
        starX = (int) (transformComp.pos.x);
        endX = (int) (transformComp.pos.x + boundsComp.width);

        getTiles(starX, startY, endX, endY, tiles);
        
        entityRect.y += movementComp.vel.y * deltaTime;

        mapCollisionComponent.grounded = false;

        for(Rectangle tile : tiles) {
            if(entityRect.overlaps(tile)) {
                if(movementComp.vel.y > 0) {
                    transformComp.pos.y = tile.y - boundsComp.height;
                } else {
                	mapCollisionComponent.grounded = true;
                    transformComp.pos.y = tile.y + tile.height;
                }
                
                // Get bottom block
                for(MapLayer l : map.getLayers()) {
                	TiledMapTileLayer layer;
                	try {
                		layer = (TiledMapTileLayer) l;
					} catch (Exception e) {
						continue;
					}
                		
                	if(layer.getName().equals(MapCollisionComponent.BACK_LAYER))
                		continue;
                	
                	Cell cell = layer.getCell((int) tile.x, (int) tile.y);
                	
                	if(cell != null)  {
                		if(layer.getName().equals(MapCollisionComponent.FLIP_LAYER)) {
                			mapCollisionMapper.get(entity).bottomCell = CellType.FLIP;
                		} else if(layer.getName().equals(MapCollisionComponent.SOLID_LAYER)){
                			mapCollisionMapper.get(entity).bottomCell = CellType.SOLID;
                		} else if(layer.getName().equals(MapCollisionComponent.VEL_LAYER)) {
                			mapCollisionMapper.get(entity).bottomCell = CellType.VEL;
                		}
                	}
                }
                
                movementComp.vel.y = 0;

                break;
            }
            	
        }
        
        if(tiles.size == 0) {
        	mapCollisionMapper.get(entity).bottomCell = CellType.NONE;
        }

		RectPool.getInstance().free(entityRect);
	}
	
	private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
		TiledMapTileLayer solidLayer = (TiledMapTileLayer) map.getLayers().get(CellType.SOLID.toString());
		TiledMapTileLayer flipLayer = (TiledMapTileLayer) map.getLayers().get(CellType.FLIP.toString());

		RectPool.getInstance().freeAll(tiles);

		tiles.clear();
		
		for(MapLayer l : map.getLayers()) {

        	TiledMapTileLayer layer;
        	try {
        		layer = (TiledMapTileLayer) l;
			} catch (Exception e) {
				continue;
			}
        	
        	String layerName = layer.getName();
        	if(layerName.equals(MapCollisionComponent.BACK_LAYER) || layerName.equals(MapCollisionComponent.SKY_LAYER))
        		continue;

        	for(int y = startY; y <= endY; y++) {
    			
    			for(int x = startX; x <= endX; x++) {
    				Cell cell = layer.getCell(x, y);
    				
    				if(cell != null) {
    					Rectangle rect = RectPool.getInstance().obtain();
    					rect.set(x, y, 1, 1);
    					tiles.add(rect);
    				}

    			}

    		}

        }
		
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public int getMapWidth() {
		if(map == null) return 0;
		
		return Integer.parseInt(map.getProperties().get("width").toString());
		
	}

}
