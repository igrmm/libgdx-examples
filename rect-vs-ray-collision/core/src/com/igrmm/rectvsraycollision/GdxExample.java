package com.igrmm.rectvsraycollision;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GdxExample extends ApplicationAdapter {
	OrthographicCamera cam;
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer shape;

	@Override
	public void create() {
		cam = new OrthographicCamera();
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		font = new BitmapFont();
	}

	@Override
	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		shape.setProjectionMatrix(cam.combined);

		batch.begin();
		font.draw(batch, mx() + " " + my(), 0, Gdx.graphics.getHeight());
		batch.end();

		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.rect(mx(), my(), 100.0f, 100.0f);
		shape.end();
	}

	public float mx() {
		return (float) Gdx.input.getX();
	}

	public float my() {
		return (float) Gdx.graphics.getHeight() - Gdx.input.getY();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
