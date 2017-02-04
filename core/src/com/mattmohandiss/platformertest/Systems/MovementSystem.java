package com.mattmohandiss.platformertest.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mattmohandiss.platformertest.Components.MovementComponent;
import com.mattmohandiss.platformertest.Components.PhysicsComponent;

/**
 * Created by Matthew on 9/5/16.
 */
public class MovementSystem extends IteratingSystem {
	public MovementSystem() {
		super(Family.all(MovementComponent.class, PhysicsComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

	}
}
