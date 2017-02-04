package com.mattmohandiss.platformertest.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mattmohandiss.platformertest.Components.StateMachineComponent;
import com.mattmohandiss.platformertest.Mappers;

/**
 * Created by Matthew on 9/5/16.
 */
public class StateMachineSystem extends IteratingSystem {
	public StateMachineSystem() {
		super(Family.all(StateMachineComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Mappers.stateMachine.get(entity).stateMachine.update();
	}
}
