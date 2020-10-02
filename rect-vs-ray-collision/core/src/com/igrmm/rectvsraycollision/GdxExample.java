package com.igrmm.rectvsraycollision;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class GdxExample extends ApplicationAdapter {
	OrthographicCamera cam;
	ShapeRenderer shape;

	Rectangle rect;
	Vector2 rayOrigin;
	Vector2 rayDir;
	Vector2 contactPoint;

	@Override
	public void create() {
		cam = new OrthographicCamera();
		shape = new ShapeRenderer();
		rect = new Rectangle(0.0f, 0.0f, 100.0f, 100.0f);
		rayOrigin = new Vector2();
		rayDir = new Vector2();
		contactPoint = new Vector2();
	}

	@Override
	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
		rect.setPosition(width / 2.0f - rect.width / 2.0f, height / 2.0f - rect.height / 2.0f);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		shape.setProjectionMatrix(cam.combined);
		rayDir.set(mx() - rayOrigin.x, my() - rayOrigin.y);

		//Drawing
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.setColor(Color.WHITE);
		if (rayVsRect(rayOrigin, rayDir, rect, contactPoint)) shape.setColor(Color.BLUE);
		shape.rect(rect.x, rect.y, rect.width, rect.height);
		shape.setColor(Color.RED);
		shape.line(rayOrigin.x, rayOrigin.y, mx(), my());
		shape.circle(contactPoint.x, contactPoint.y, 5);
		shape.end();
	}

	public boolean rayVsRect(Vector2 rayOrigin, Vector2 rayDir, Rectangle rect, Vector2 contactPoint) {
		contactPoint.set(0, 0);

		Vector2 targetPos = new Vector2();
		rect.getPosition(targetPos);
		Vector2 targetSize = new Vector2();
		rect.getSize(targetSize);

		Vector2 invDir = new Vector2(1.0f / rayDir.x, 1.0f / rayDir.y);
		Vector2 tNear = (targetPos.cpy().sub(rayOrigin)).scl(invDir);
		Vector2 tFar = (targetPos.cpy().add(targetSize).sub(rayOrigin)).scl(invDir);

		if (tNear.x > tFar.x) {
			float tmp = tNear.x;
			tNear.x = tFar.x;
			tFar.x = tmp;
		}

		if (tNear.y > tFar.y) {
			float tmp = tNear.y;
			tNear.y = tFar.y;
			tFar.y = tmp;
		}

		if (tNear.x > tFar.y || tNear.y > tFar.x) return false;

		float tHitNear = Math.max(tNear.x, tNear.y);
		if (tHitNear > 1.0f) return false;

		float tHitFar = Math.min(tFar.x, tFar.y);
		if (tHitFar < 0) return false;

		contactPoint.set(rayOrigin.cpy().add(rayDir.cpy().scl(tHitNear)));

		return true;
	}

	public float mx() {
		return (float) Gdx.input.getX();
	}

	public float my() {
		return (float) Gdx.graphics.getHeight() - Gdx.input.getY();
	}

	@Override
	public void dispose() {
		shape.dispose();
	}
}
