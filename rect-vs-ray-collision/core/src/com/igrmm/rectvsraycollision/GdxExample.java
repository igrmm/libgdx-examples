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
	Vector2 contactNormal;

	@Override
	public void create() {
		cam = new OrthographicCamera();
		shape = new ShapeRenderer();
		rect = new Rectangle(0.0f, 0.0f, 100.0f, 100.0f);
		rayOrigin = new Vector2();
		rayDir = new Vector2();
		contactPoint = new Vector2();
		contactNormal = new Vector2();
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
		if (rayVsRect(rayOrigin, rayDir, rect, contactPoint, contactNormal)) {
			shape.setColor(Color.BLUE);
			shape.line(contactPoint.x, contactPoint.y, (contactPoint.x + contactNormal.x * 20.0f), (contactPoint.y + contactNormal.y * 20.0f));
			shape.setColor(Color.WHITE);
		}
		shape.rect(rect.x, rect.y, rect.width, rect.height);
		shape.setColor(Color.RED);
		shape.line(rayOrigin.x, rayOrigin.y, mx(), my());
		shape.circle(contactPoint.x, contactPoint.y, 5);
		shape.end();
	}

	public boolean rayVsRect(Vector2 ray0, Vector2 ray1, Rectangle rect, Vector2 contactPoint, Vector2 contactNormal) {

		/*
			    Ray equation
		        contact = ray0 + ray1 * tMin
		*/

		// t value for ray to contact line defined by rect.x
		float t0X = (rect.x - ray0.x) / ray1.x;

		// t value for ray to contact line defined by rect.y
		float t0Y = (rect.y - ray0.y) / ray1.y;

		// t value for ray to contact line defined by (rect.x + rect.width)
		float t1X = (rect.x + rect.width - ray0.x) / ray1.x;

		// t value for ray to contact line defined by (rect.y + rect.height)
		float t1Y = (rect.y + rect.height - ray0.y) / ray1.y;

		//swap values
		if (t0X > t1X) {
			float tmp = t0X;
			t0X = t1X;
			t1X = tmp;
		}

		//swap values
		if (t0Y > t1Y) {
			float tmp = t0Y;
			t0Y = t1Y;
			t1Y = tmp;
		}

		//non-contact condition
		if (t0X > t1Y || t0Y > t1X) return false;

		//t value for ray to first contact with rectangle
		float tMin = Math.max(t0X, t0Y);
		if (tMin > 1.0f) return false;

		//t value for ray to last contact with rectangle
		float tMax = Math.min(t1X, t1Y);
		if (tMax < 0) return false;

		//set contact point from ray equation with t value
		contactPoint.set(ray0.x + (ray1.x * tMin), ray0.y + (ray1.y * tMin));

		//set contact normal for collision solving
		if (t0X > t0Y) {
			if (ray0.x < 0)
				contactNormal.set(1, 0);
			else
				contactNormal.set(-1, 0);
		} else if (t0X < t0Y) {
			if (ray0.y < 0)
				contactNormal.set(0, 1);
			else
				contactNormal.set(0, -1);
		}

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

	private class BoundingBox {
		Rectangle rectangle;
		Vector2 velocity;

		public BoundingBox(float x, float y, float width, float height) {
			rectangle = new Rectangle(x, y, width, height);
			velocity = new Vector2();
		}
	}
}
