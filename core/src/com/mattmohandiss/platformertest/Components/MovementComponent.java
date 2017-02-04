package com.mattmohandiss.platformertest.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.mattmohandiss.platformertest.RayCast;
import com.mattmohandiss.platformertest.rayCastType;

/**
 * Created by Matthew on 9/5/16.
 */
public class MovementComponent implements Component {
	public float xAcceleration = 0;
	public float airAcceleration = 1 / 4;
	public float maxHorizontalSpeed = 30;
	public float maxVerticalSpeed = 20;
	public boolean grounded = true;

	public Array<RayCast> rayCasts = new Array<>();

	public Array<RayCast> getRayCastsOfType(rayCastType type) {
		Array<RayCast> rayCastsOfType = new Array<>();
		for (RayCast rayCast :
				rayCasts) {
			if (rayCast.type == type) {
				rayCastsOfType.add(rayCast);
			}
		}
		return rayCastsOfType;
	}
}
