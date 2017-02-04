package com.mattmohandiss.platformertest;

import com.badlogic.ashley.core.ComponentMapper;
import com.mattmohandiss.platformertest.Components.MovementComponent;
import com.mattmohandiss.platformertest.Components.PhysicsComponent;
import com.mattmohandiss.platformertest.Components.StateMachineComponent;

/**
 * Created by Matthew on 9/5/16.
 */
public class Mappers {
	public static ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
	public static ComponentMapper<StateMachineComponent> stateMachine = ComponentMapper.getFor(StateMachineComponent.class);
	public static ComponentMapper<MovementComponent> movement = ComponentMapper.getFor(MovementComponent.class);
}
