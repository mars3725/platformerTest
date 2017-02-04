package com.mattmohandiss.platformertest;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

/**
 * Created by Matthew on 9/5/16.
 */
public class RayCast {
	public Vector2 origin = new Vector2();
	public Vector2 end = new Vector2();
	public rayCastType type;
	public boolean rayCastReturned = false;
	public Vector2 rayCastNormal = new Vector2();
	RayCastCallback rayCastCallback = new RayCastCallback() {
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			rayCastReturned = true;
			rayCastNormal = normal;
			return 0.25f;
		}
	};
	private MainGame game;
	private Vector2 localOrigin = new Vector2();
	private Vector2 localEnd = new Vector2();

	public RayCast(Vector2 origin, Vector2 end, MainGame game, rayCastType type) {
		this.localOrigin = origin;
		this.localEnd = end;
		this.game = game;
		this.type = type;
	}

	public void center(Vector2 position, float rotation) {
		Vector2 localOriginTemp = localOrigin.cpy();
		Vector2 localEndTemp = localOrigin.cpy();
		localOriginTemp.set(localOrigin.x * MathUtils.cos(rotation) - localOrigin.y * MathUtils.sin(rotation), localOrigin.x * MathUtils.sin(rotation) + localOrigin.y * MathUtils.cos(rotation));
		localEndTemp.set(localEnd.x * MathUtils.cos(rotation) - localEnd.y * MathUtils.sin(rotation), localEnd.x * MathUtils.sin(rotation) + localEnd.y * MathUtils.cos(rotation));

		origin.set(position.add(localOriginTemp));
		end.set(position.add(localEndTemp));
	}

	public float getSlope(RayCast otherRaycast) {
		if (isTriggered() && otherRaycast.isTriggered()) {
			game.world.rayCast(rayCastCallback, origin, end);
			float firstAngle = rayCastNormal.cpy().angleRad();
			game.world.rayCast(rayCastCallback, otherRaycast.origin, otherRaycast.end);
			return (firstAngle + rayCastNormal.angleRad()) / 2 - (MathUtils.PI / 2);

		} else if (isTriggered()) {
			game.world.rayCast(rayCastCallback, origin, end);
			return rayCastNormal.angleRad() - (MathUtils.PI / 2);

		} else if (otherRaycast.isTriggered()) {
			game.world.rayCast(rayCastCallback, otherRaycast.origin, otherRaycast.end);
			return rayCastNormal.angleRad() - (MathUtils.PI / 2);

		} else {
			return 0;
		}

//		if (isTriggered()) {
//			game.world.rayCast(rayCastCallback, origin, end);
//			return rayCastNormal.angleRad() - (MathUtils.PI/2);
//		} else if (otherRaycast.isTriggered()) {
//			game.world.rayCast(rayCastCallback, otherRaycast.origin, otherRaycast.end);
//			return rayCastNormal.angleRad() - (MathUtils.PI/2);
//		} else {
//			return 0;
//		}
	}

	public boolean isTriggered() {
		rayCastReturned = false;
		game.world.rayCast(rayCastCallback, origin, end);
		return rayCastReturned;
	}
}

