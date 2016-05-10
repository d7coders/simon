package com.Delta7Coders.Simon;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.content.Intent;
import android.util.Log;

public class MainActivity extends SimpleBaseGameActivity {

	private static int CAMERA_WIDTH = 480;
	private static int CAMERA_HEIGHT = 800;
	private ITextureRegion mBackgroundTextureRegion,G,R,B,Y;
	private Sprite mG, mR, mB,mY;
	private Sound snd1,snd2,snd3,snd4,snd_lost;
	Camera camera ;
	private ArrayList<Integer> ar;
	private int count=1;
	private int player_turn=0;
	private TimerTask tt;
	Timer timer ;
	int i=0;
	private HUD gameHUD;
	private Font font;
	private Text scoreText;
	int score=0;
	int touch_enable=0;
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		//createControllers();
	
		
		
		EngineOptions en= new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
		    new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		
		en.getAudioOptions().setNeedsSound(true);
		return en;
	}

	@Override
	protected void onCreateResources(){
		// TODO Auto-generated method stub
		try {
		    // 1 - Set up bitmap textures
		    ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/bd.jpg");
		        }
		    });
		 
		    ITexture G = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/green1.png");
		        }});
		    
		    ITexture R = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/red1.png");
		        }});
		    
		    ITexture B = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/blue1.png");
		        }});
		    
		    ITexture Y = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/yellow1.png");
		        }});
		    
		    backgroundTexture.load();
		    G.load();
		    R.load();
		    B.load();
		    Y.load();
		    
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		    this.B=TextureRegionFactory.extractFromTexture(B);
		    this.R=TextureRegionFactory.extractFromTexture(R);
		    this.G=TextureRegionFactory.extractFromTexture(G);
		    this.Y=TextureRegionFactory.extractFromTexture(Y);
		    
		    
		   
		    
		    
		} catch (IOException e) {
		    Debug.e(e);
		}
		
		// load sounds
		try
		  {
		   snd1 = SoundFactory.createSoundFromAsset(this.getSoundManager(), this.getApplicationContext(),
				   	 "sfx/snd1.ogg");
		   snd2 = SoundFactory.createSoundFromAsset(this.getSoundManager(), this.getApplicationContext(),
				     "sfx/snd2.ogg");
		   snd3 = SoundFactory.createSoundFromAsset(this.getSoundManager(), this.getApplicationContext(),
				     "sfx/snd3.ogg");
		   snd4 = SoundFactory.createSoundFromAsset(this.getSoundManager(), this.getApplicationContext(),
				     "sfx/snd4.ogg");
		   
		   snd_lost = SoundFactory.createSoundFromAsset(this.getSoundManager(), this.getApplicationContext(),
				     "sfx/lost.ogg");
		   
		  } catch (IOException e)
		  {
		   e.printStackTrace();
		  }
		
			
		font = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 256, 256, this.getAssets(),
			    "font/font.ttf", 46, true, android.graphics.Color.WHITE);
			  font.load();
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		final Scene scene = new Scene();
		
		//createControllers();
		
		
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
	
		
		
		
		mB = new Sprite(100, 270, this.B, getVertexBufferObjectManager()){
			public boolean onAreaTouched(org.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
				if(touch_enable==0)
					return false;
				
				if (pSceneTouchEvent.isActionUp())
		        {
	
					B_pressed();
			
		        }
		        return true;
			};};
		mR = new Sprite(250, 270, this.R, getVertexBufferObjectManager()){
			public boolean onAreaTouched(org.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
				if(touch_enable==0)
					return false;
				
				if (pSceneTouchEvent.isActionUp())
		        {
					R_pressed();
		        }
		        return true;
			};};
			
		mG = new Sprite(100, 410, this.G, getVertexBufferObjectManager()){
			public boolean onAreaTouched(org.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
				if(touch_enable==0)
					return false;
				
				if (pSceneTouchEvent.isActionUp())
		        {
					G_pressed();
		        }
		        return true;
			};};
		mY = new Sprite(250, 410, this.Y, getVertexBufferObjectManager()){
			public boolean onAreaTouched(org.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
				if(touch_enable==0)
					return false;
				
				if (pSceneTouchEvent.isActionUp())
		        {
					Y_pressed();
		        }
		        return true;
			};};
		
		
		// touches
		scene.registerTouchArea(mB);
		scene.registerTouchArea(mR);
		scene.registerTouchArea(mG);
		scene.registerTouchArea(mY);

		
		
		
		
		
		scene.attachChild(backgroundSprite);
		
		//scene.setBackground(new Background(Color.))
		
		scene.attachChild(mB);
		scene.attachChild(mR);
		scene.attachChild(mG);
		scene.attachChild(mY);
		
		///////////////////////////////////

		
		////////////////////////////////////
//		tt = new TimerTask() {
//			int i=0;
//			
//			@Override
//			public void run() {
//				
//				
//				
//				switch (ar.get(i)) {
//				case 1:
//					B_pressed();
//					break;
//				case 2:
//					R_pressed();
//					break;
//				case 3:
//					G_pressed();
//					break;
//				case 4:
//					Y_pressed();
//					break;
//
//				default:
//					break;
//				}
//				i++;
//				if(i+1>ar.size()){
//					tt.cancel();
//					timer.purge();
//					set_player_turn(1);
//				}
//			}
//				
//			
//		};
		
		ar = new ArrayList<Integer>();
//		timer = new Timer();
		
		play();
		
		createHUD();
		return scene;
	}

	
	
	private void createControllers()
	{
	    HUD yourHud = new HUD();
	    
	    final Rectangle left = new Rectangle(20, 200, 60, 60, getVertexBufferObjectManager())
	    {
	        public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	        {
	            if (touchEvent.isActionUp())
	            {
	                // move player left
	            	Log.d("samad", "LEFT");
	            }
	            return true;
	        };
	    };
	    
	    final Rectangle right = new Rectangle(100, 200, 60, 60, getVertexBufferObjectManager())
	    {
	        public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	        {
	            if (touchEvent.isActionUp())
	            {
	                // move player right
	            	Log.d("samad", "Right");
	            }
	            return true;
	        };
	    };
	    
	    yourHud.registerTouchArea(left);
	    yourHud.registerTouchArea(right);
	    yourHud.attachChild(left);
	    yourHud.attachChild(right);
	    
	    camera.setHUD(yourHud);
	}
	
	void B_pressed(){
		
		snd1.play();
		mB.setAlpha(0.5f);
	       
	        this.getEngine().registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback()
	        	{                      
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler)
	            {
	            	mB.setAlpha(1.0f);
	            } }));
	        
	        
	     if(player_turn==1){
	    	 
	    	 if(ar.get(count-1)==1){
	    		 
	    		 count++;
	    		 if(count-1==ar.size()){
	    			 set_player_turn(0);
	    			 cpu_turn();
	    		 }
	    		 
	    	 }
	    	 else
	    		 lose();
	    	 
	     }
	}
	
	
	void R_pressed(){
		
		snd2.play();
		mR.setAlpha(0.5f);
	       
	        this.getEngine().registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback()
	        {                      
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler)
	            {
	            	mR.setAlpha(1.0f);
	            } }));
	        
	        if(player_turn==1){
		    	 
		    	 if(ar.get(count-1)==2){
		    		 
		    		 count++;
		    		 if(count-1==ar.size()){
		    			 set_player_turn(0);
		    			 cpu_turn();
		    		 }
		    		 
		    	 }
		    	 else
		    		 lose();
		    	 
		     }
	        
	        
	}
	
	
	void G_pressed(){
		
		snd3.play();
		mG.setAlpha(0.5f);
	       
	        this.getEngine().registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback()
	        {                      
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler)
	            {
	            	mG.setAlpha(1.0f);
	            } }));
	        
	        if(player_turn==1){
		    	 
		    	 if(ar.get(count-1)==3){
		    		 
		    		 count++;
		    		 if(count-1==ar.size()){
		    			 set_player_turn(0);
		    			 cpu_turn();
		    		 }
		    		 
		    	 }
		    	 else
		    		 lose();
		    	 
		     }
	}

	
	void Y_pressed(){
		
		
		snd4.play();
		mY.setAlpha(0.5f);
	       
	        this.getEngine().registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback()
	        {                      
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler)
	            {
	            	mY.setAlpha(1.0f);
	            } }));
	        
	        if(player_turn==1){
		    	 
		    	 if(ar.get(count-1)==4){
		    		
		    		 count++;
		    		 if(count-1==ar.size()){
		    			 set_player_turn(0);
		    			 cpu_turn();
		    		 }
		    		
		    	 }
		    	 else
		    		 lose();
		    	 
		     }
	}

	
	void play(){
		
		add();
		
		i=0;
		
		 this.getEngine().registerUpdateHandler( new TimerHandler(1.0f, new ITimerCallback()
	        {        
			 
			 	
			 
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler)
	            {
	            	
	            	switch (ar.get(i)) {
					case 1:
						B_pressed();
						break;
					case 2:
						R_pressed();
						break;
					case 3:
						G_pressed();
						break;
					case 4:
						Y_pressed();
						break;

					default:
						break;
					}
	            	
					i++;
					Log.d("samad","i="+Integer.toString(i)+"ar="+Integer.toString(ar.get(i-1)));
					if(i+1>ar.size()){
						set_player_turn(1);
					}
					else{
						pTimerHandler.reset();
					}
	            	
	            	
	            } }));
		
		
		
		//timer.schedule(tt,1000,1000);

	}
	
	
	
	void add(){
		
		Random rd = new Random();
		ar.add(rd.nextInt(4)+1);
	}
	
	void set_player_turn(int i){
		if (i==0)
			player_turn=0;
		else{
			player_turn=1;
			touch_enable=1;
		}
	}
	
	void cpu_turn(){
	
		score+=10;
		touch_enable=0;
		scoreText.setText("Score: " + score  );
		count=1;
		
		play();
	}
	
	void lose(){

		
		Intent in = new Intent(MainActivity.this,gameOver.class);
		in.putExtra("score", Integer.toString(score));
		startActivity(in);
		finish();
		
	}
	
	
	private void createHUD()
	{
	    gameHUD = new HUD();
	    
	 // CREATE SCORE TEXT
	    scoreText=new Text(150, 700, font, "SCORE:0123456789", this.getVertexBufferObjectManager());
	      
	    scoreText.setText("Score: 0");
	    gameHUD.attachChild(scoreText);
	    
	    camera.setHUD(gameHUD);
	}
}
