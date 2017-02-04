package com.mattmohandiss.platformertest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * Created by Matthew on 9/5/16.
 */
public class GameLauncher extends Game {
	public OrthographicCamera camera;
	private FillViewport viewport;

	@Override
	public void create() {
		camera = new OrthographicCamera(100, 50);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		viewport = new FillViewport(camera.viewportWidth, camera.viewportHeight, camera);
		setScreen(new MainGame(this));
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
}
