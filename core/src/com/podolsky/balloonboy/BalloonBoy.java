package com.podolsky.balloonboy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.concurrent.TimeUnit;

public class BalloonBoy extends ApplicationAdapter {
	private Texture balloonImage;
    private OrthographicCamera camera;
	private Rectangle balloon;

	private SpriteBatch batch;

	private long lastTouchTime;

	@Override
	public void create () {
	    balloonImage = new Texture(Gdx.files.internal("balloon.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        balloon = new Rectangle();
        balloon.x = 800/2 - 64/2;
        balloon.y = 20;
        balloon.width = 64;
        balloon.height = 64;

        batch = new SpriteBatch();

        lastTouchTime = 0;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(balloonImage, balloon.x, balloon.y);
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
	}
	
	@Override
	public void dispose () {
	}
}
