package com.mattmohandiss.platformertest.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mattmohandiss.platformertest.Components.MovementComponent;
import com.mattmohandiss.platformertest.Components.PhysicsComponent;
import com.mattmohandiss.platformertest.MainGame;
import com.mattmohandiss.platformertest.Mappers;
import com.mattmohandiss.platformertest.RayCast;

/**
 * Created by Matthew on 9/5/16.
 */
public class DebugSystem extends IteratingSystem {
	public ShapeRenderer shapeRenderer = new ShapeRenderer();
	OrthographicCamera camera;

	public DebugSystem(MainGame game) {
		super(Family.all(MovementComponent.class, PhysicsComponent.class).get());
		camera = game.launcher.camera;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

		for (int i = 0; i < Mappers.movement.get(entity).rayCasts.size; i++) {
			RayCast rayCast = Mappers.movement.get(entity).rayCasts.get(i);
			if (rayCast.isTriggered()) {
				shapeRenderer.setColor(Color.GREEN);
			} else {
				shapeRenderer.setColor(Color.RED);
			}
			shapeRenderer.line(rayCast.origin, rayCast.end);
		}
		shapeRenderer.end();
	}
}
