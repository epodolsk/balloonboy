package com.podolsky.balloonboy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import org.w3c.dom.css.Rect;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class BalloonBoy extends ApplicationAdapter {
	private Texture balloonImage;
	private Texture arrowImage;
    private OrthographicCamera camera;
	private Rectangle balloon;
	private Array<Rectangle> arrows;

	private SpriteBatch batch;

	private long lastTouchTime;

	private long lastArrowTime;

	private boolean death = false;



	@Override
	public void create () {
	    balloonImage = new Texture(Gdx.files.internal("balloon.png"));
        arrowImage = new Texture(Gdx.files.internal("arrow.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        balloon = new Rectangle();
        balloon.x = 800/2 - 64/2;
        balloon.y = 20;
        balloon.width = 64;
        balloon.height = 64;

        batch = new SpriteBatch();

        lastTouchTime = 0;

        arrows = new Array<Rectangle>();
        spawnArrow();
	}

    @Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(!death) {
            batch.draw(balloonImage, balloon.x, balloon.y);
        }
        for(Rectangle arrow : arrows) {
            batch.draw(arrowImage, arrow.x, arrow.y);
        }
        batch.end();

        if(Gdx.input.justTouched()) {
            lastTouchTime = TimeUtils.nanoTime();
            balloon.y += 10;
        }

        if(TimeUtils.nanoTime() - lastTouchTime < 1E8) {
            balloon.y += 10;
        } else {
            balloon.y -= 5;
        }

        if(balloon.y < 20) {
            balloon.y = 20;
        }

        if(balloon.y > 480-64-20) {
            balloon.y = 480-64-20;
        }

        for(Iterator<Rectangle> iter = arrows.iterator(); iter.hasNext();) {
            Rectangle arrow = iter.next();
            arrow.x -= 100 * Gdx.graphics.getDeltaTime();
            if(arrow.x < -367) {
                iter.remove();
            }
            if(arrow.overlaps(balloon)) {
                death = true;
            }
        }

        if(TimeUtils.nanoTime() - lastArrowTime > 3000000000L) {
            spawnArrow();
        }
	}
	
	@Override
	public void dispose () {
	}

    private void spawnArrow() {
	    Rectangle arrow = new Rectangle();
        arrow.x = 800;
        arrow.y = MathUtils.random(41, 480-41);
        arrow.height = 41;
        arrow.width = 367;
        arrows.add(arrow);
        lastArrowTime = TimeUtils.nanoTime();
    }
}
