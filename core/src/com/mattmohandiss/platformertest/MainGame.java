package com.mattmohandiss.platformertest;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mattmohandiss.platformertest.Components.MovementComponent;
import com.mattmohandiss.platformertest.Components.PhysicsComponent;
import com.mattmohandiss.platformertest.Components.StateMachineComponent;
import com.mattmohandiss.platformertest.Systems.DebugSystem;
import com.mattmohandiss.platformertest.Systems.MovementSystem;
import com.mattmohandiss.platformertest.Systems.StateMachineSystem;

public class MainGame extends ScreenAdapter implements InputProcessor {
	public GameLauncher launcher;
	public World world = new World(new Vector2(0, -30), true);
	public Engine engine = new Engine();
	public Entity player = new Entity();
	Box2DDebugRenderer debugRenderer;

	public MainGame(GameLauncher launcher) {
		this.launcher = launcher;
		debugRenderer = new Box2DDebugRenderer();
		Gdx.input.setInputProcessor(this);

		engine.addSystem(new StateMachineSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new DebugSystem(this));

		createCharacter();
		createBorder(300, 250);
		createWorld();
	}

	public void createWorld() {

		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(0, 0);
		vertices[1] = new Vector2(20, 0);
		vertices[2] = new Vector2(15, 12);
		vertices[3] = new Vector2(0, 2);
		createPolygon(vertices, new Vector2(225, 0));

		vertices = new Vector2[3];
		vertices[0] = new Vector2(0, 0);
		vertices[1] = new Vector2(5, 0);
		vertices[2] = new Vector2(0, 5);
		createPolygon(vertices, new Vector2(100, 40));

		vertices = new Vector2[3];
		vertices[0] = new Vector2(0, 0);
		vertices[1] = new Vector2(50, 0);
		vertices[2] = new Vector2(25, 30);
		vertices[2] = new Vector2(25, 10);
		createPolygon(vertices, new Vector2(25, 0));

		vertices = new Vector2[4];
		vertices[0] = new Vector2(0, 0);
		vertices[1] = new Vector2(0, 10);
		vertices[2] = new Vector2(20, 10);
		vertices[3] = new Vector2(20, 0);
		createPolygon(vertices, new Vector2(150, 0));

		vertices = new Vector2[4];
		vertices[0] = new Vector2(0, 0);
		vertices[1] = new Vector2(0, 5);
		vertices[2] = new Vector2(10, 5);
		vertices[3] = new Vector2(10, 0);
		createPolygon(vertices, new Vector2(75, 20));

		vertices = new Vector2[3];
		vertices[0] = new Vector2(0, 0);
		vertices[1] = new Vector2(50, 0);
		vertices[2] = new Vector2(50, 10);
		createPolygon(vertices, new Vector2(100, 0));

		vertices = new Vector2[3];
		vertices[0] = new Vector2(50, 0);
		vertices[1] = new Vector2(0, 0);
		vertices[2] = new Vector2(0, 10);
		createPolygon(vertices, new Vector2(170, 0));
	}

	private void createPolygon(Vector2[] vertices, Vector2 position) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		Body body = world.createBody(bodyDef);
		ChainShape loop = new ChainShape();
		loop.createLoop(vertices);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = loop;
		fixtureDef.friction = 1;
		body.createFixture(fixtureDef);
		loop.dispose();
	}

	public void createCharacter() {
		PhysicsComponent physicsComponent = new PhysicsComponent();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(10, 10);
		bodyDef.fixedRotation = true;
		physicsComponent.body = world.createBody(bodyDef);
		PolygonShape box = new PolygonShape();
		box.setAsBox(1, 2.5f, new Vector2(0, 0), 0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 1020;
		//fixtureDef.friction = 1;
		physicsComponent.body.createFixture(fixtureDef);
//		CircleShape circle = new CircleShape();
//		circle.setPosition(new Vector2(0, -2.5f));
//		circle.setRadius(1);
//		physicsComponent.body.createFixture(circle, 1020);
		box.dispose();
		player.add(physicsComponent);

		StateMachineComponent stateMachineComponent = new StateMachineComponent();
		stateMachineComponent.stateMachine = new DefaultStateMachine<>(player, PlayerState.Idle);
		player.add(stateMachineComponent);

		MovementComponent movementComponent = new MovementComponent();
		movementComponent.rayCasts.add(new RayCast(new Vector2(-1, -2.5f), new Vector2(-.25f, -0f), this, rayCastType.leftHorizontal));
		movementComponent.rayCasts.peek().center(physicsComponent.body.getPosition(), physicsComponent.body.getAngle());
		movementComponent.rayCasts.add(new RayCast(new Vector2(1, -2.5f), new Vector2(.25f, -0f), this, rayCastType.rightHorizontal));
		movementComponent.rayCasts.peek().center(physicsComponent.body.getPosition(), physicsComponent.body.getAngle());
		movementComponent.rayCasts.add(new RayCast(new Vector2(-1, -2.5f), new Vector2(0, -.75f), this, rayCastType.leftVertical));
		movementComponent.rayCasts.peek().center(physicsComponent.body.getPosition(), physicsComponent.body.getAngle());
		movementComponent.rayCasts.add(new RayCast(new Vector2(1, -2.5f), new Vector2(0, -.75f), this, rayCastType.rightVertical));
		movementComponent.rayCasts.peek().center(physicsComponent.body.getPosition(), physicsComponent.body.getAngle());
		player.add(movementComponent);

		engine.addEntity(player);
	}

	private void createBorder(float width, float height) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(width / 2, height / 2);
		Body body = world.createBody(bodyDef);
		ChainShape loop = new ChainShape();
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(-width / 2, -height / 2);
		vertices[1] = new Vector2(-width / 2, height / 2);
		vertices[2] = new Vector2(width / 2, height / 2);
		vertices[3] = new Vector2(width / 2, -height / 2);
		loop.createLoop(vertices);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = loop;
		//fixtureDef.friction = 1;
		body.createFixture(fixtureDef);
		loop.dispose();
	}

	public void dispose() {

	}

	public void render(float delta) {
		engine.update(delta);
		Vector2 position = Mappers.physics.get(player).body.getPosition();
		launcher.camera.position.set(position.x, position.y, 0);
		launcher.camera.update();
		world.step(1 / 60f, 6, 2);
		debugRenderer.render(world, launcher.camera.combined);
	}

	@Override
	public boolean keyDown(int keycode) {
		StateMachine<Entity, PlayerState> playerState = Mappers.stateMachine.get(player).stateMachine;

		switch (keycode) {
			case Input.Keys.UP:
				if (Mappers.movement.get(player).grounded) {
					playerState.changeState(PlayerState.Up);
				}
				break;
			case Input.Keys.RIGHT:
				playerState.changeState(PlayerState.Right);
				break;
			case Input.Keys.LEFT:
				playerState.changeState(PlayerState.Left);
				break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Mappers.stateMachine.get(player).stateMachine.changeState(PlayerState.Idle);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
