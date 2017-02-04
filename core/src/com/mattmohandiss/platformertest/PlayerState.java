package com.mattmohandiss.platformertest;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew on 9/5/16.
 */
public enum PlayerState implements State<Entity> {
	Left() {
		@Override
		public void enter(Entity entity) {
		}

		@Override
		public void update(Entity entity) {
			Vector2 linearVel = Mappers.physics.get(entity).body.getLinearVelocity();
			if (Math.abs(linearVel.x) < Mappers.movement.get(entity).maxHorizontalSpeed) {
				Mappers.physics.get(entity).body.setLinearVelocity(linearVel.x - 1, linearVel.y);
			}
		}

		@Override
		public void exit(Entity entity) {
		}
	},

	Right() {
		@Override
		public void enter(Entity entity) {
		}

		@Override
		public void update(Entity entity) {
			Vector2 linearVel = Mappers.physics.get(entity).body.getLinearVelocity();
			if (Math.abs(linearVel.x) < Mappers.movement.get(entity).maxHorizontalSpeed) {
				Mappers.physics.get(entity).body.setLinearVelocity(linearVel.x + 1, linearVel.y);
			}
		}

		@Override
		public void exit(Entity entity) {
		}
	},

	Up() {
		float maxVel;

		@Override
		public void enter(Entity entity) {
			maxVel = Mappers.movement.get(entity).maxVerticalSpeed + Mappers.physics.get(entity).body.getLinearVelocity().y;
		}

		@Override
		public void update(Entity entity) {
			Vector2 velocity = Mappers.physics.get(entity).body.getLinearVelocity().rotateRad(Mappers.physics.get(entity).body.getAngle());
//			if (!Mappers.movement.get(entity).rayCasts.peek().isTriggered() && Mappers.movement.get(entity).rayCasts.get(0).isTriggered()) {
//				System.out.println("left wall jump");
//				Mappers.physics.get(entity).body.setLinearVelocity(25, 20);
//			} else if (!Mappers.movement.get(entity).rayCasts.peek().isTriggered() && Mappers.movement.get(entity).rayCasts.get(1).isTriggered()) {
//				System.out.println("right wall jump");
//				Mappers.physics.get(entity).body.setLinearVelocity(-25, 20);
//			}

			if (velocity.y < maxVel) {
				velocity.y += 1;
				velocity.rotate(-Mappers.physics.get(entity).body.getAngle());
				Mappers.physics.get(entity).body.setLinearVelocity(velocity.x, velocity.y + 1);
			} else {
				Mappers.stateMachine.get(entity).stateMachine.changeState(PlayerState.Fall);
			}
		}

		@Override
		public void exit(Entity entity) {
		}
	},

	Fall() {
		@Override
		public void enter(Entity entity) {

		}

		@Override
		public void update(Entity entity) {
			if (Mappers.movement.get(entity).grounded) {
				Mappers.stateMachine.get(entity).stateMachine.changeState(Idle);
			}
		}

		@Override
		public void exit(Entity entity) {

		}
	},

	Idle() {
		@Override
		public void enter(Entity entity) {
//			Vector2 velocity = Mappers.physics.get(entity).body.getLinearVelocity().rotateRad(Mappers.physics.get(entity).body.getAngle());
//			Mappers.physics.get(entity).body.setLinearVelocity(0, velocity.y);
			if (Mappers.movement.get(entity).grounded) {
				Mappers.physics.get(entity).body.setLinearVelocity(Mappers.physics.get(entity).body.getLinearVelocity().x, 0);
			} else {
				Mappers.physics.get(entity).body.setLinearVelocity(Mappers.physics.get(entity).body.getLinearVelocity().x, Mappers.physics.get(entity).body.getLinearVelocity().y);
			}
		}

		@Override
		public void update(Entity entity) {
			float slowStep = 2;
			float deltaX = 0;
			if (Mappers.physics.get(entity).body.getLinearVelocity().x > 0) {
				deltaX = Mappers.physics.get(entity).body.getLinearVelocity().x - slowStep;
			} else if (Mappers.physics.get(entity).body.getLinearVelocity().x < 0) {
				deltaX = Mappers.physics.get(entity).body.getLinearVelocity().x + slowStep;
			}

			if (Mappers.movement.get(entity).grounded) {
				Mappers.physics.get(entity).body.setLinearVelocity(deltaX, 0);
			}

			if (Math.abs(Mappers.physics.get(entity).body.getLinearVelocity().x) <= slowStep) {
				Mappers.physics.get(entity).body.setLinearVelocity(0, Mappers.physics.get(entity).body.getLinearVelocity().y);
			}
		}

		@Override
		public void exit(Entity entity) {

		}
	};

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}
}
