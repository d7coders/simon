package com.Delta7Coders.Simon;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.content.Intent;

public class gameOver extends SimpleBaseGameActivity{

	private HUD gameHUD;
	private Font font;
	private Text scoreText;
	private Text gameOver;
	private ITextureRegion mBackgroundTextureRegion,play;
	private Sprite mplay;
	Camera camera;
	private static int CAMERA_WIDTH = 480;
	private static int CAMERA_HEIGHT = 800;
	private Sound snd_lost;
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions en= new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
			    new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
			
			en.getAudioOptions().setNeedsSound(true);
			return en;
	}

	@Override
	protected void onCreateResources() {

		try {
		    // 1 - Set up bitmap textures
		    ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/bd.jpg");
		        }
		    });
		    
		    ITexture play = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/play.png");
		        }});
		    
		    play.load();
		    this.play=TextureRegionFactory.extractFromTexture(play);
		    backgroundTexture.load();
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		    
		    
		    try
			  {
			   snd_lost = SoundFactory.createSoundFromAsset(this.getSoundManager(), this.getApplicationContext(),
					     "sfx/lost.ogg");
			   
			  } catch (IOException e)
			  {
			   e.printStackTrace();
			  }
		    
			
			font = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 256, 256, this.getAssets(),
				    "font/font.ttf", 46, true, android.graphics.Color.WHITE);
				  font.load();
		    
			} catch (IOException e) {
				    Debug.e(e);
				}
		
		
		
		
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		
		
		final Scene scene = new Scene();
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(backgroundSprite);
		
		
		
		
		mplay = new Sprite(200, 550, this.play, getVertexBufferObjectManager()){
			public boolean onAreaTouched(org.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
				
				if (pSceneTouchEvent.isActionUp())
		        {
					
				
					startActivity(new Intent(gameOver.this,MainActivity.class));
					finish();
			
		        }
		        return true;
			};};
		
				
			scene.registerTouchArea(mplay);
			scene.attachChild(mplay);
		createHUD();
		sn();
		return scene;
	}
	
	void sn(){
		
		this.getEngine().registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback()
        {                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
        
            	snd_lost.play();
            } }));
		
		
	}
	
	private void createHUD()
	{
	    gameHUD = new HUD();
	    
	 // CREATE SCORE TEXT

	    gameOver=new Text(120, 350, font, "GAME OVER", this.getVertexBufferObjectManager());
	    gameOver.setText("GAME OVER");
	    
	    scoreText=new Text(80, 450, font, "YOUR SCORE:0123456789", this.getVertexBufferObjectManager());
	    scoreText.setText("your score:"+getIntent().getExtras().getString("score"));
	    gameHUD.attachChild(gameOver);
	    gameHUD.attachChild(scoreText);
	    
	    camera.setHUD(gameHUD);
	}
}
