package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;


public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	private int score = 0;
	private int gamestate = 0;
	private   int width  ;
	private   int height;
	private int i = 0;
	private int j = 0;
	private Preferences preferences;
	private BitmapFont font;
	private int k = 0;
	int coinvelocity;
	int end_pause_time = 0;
	private boolean direction_background = true;
	private float gravity = 0.3f;
	private float velocity = 0.8f;
	ArrayList<Integer> coinxs = new ArrayList<>();
	ArrayList<Integer> coinys = new ArrayList<>();
	int coinCount = 0;
	ArrayList<Integer> bombsx = new ArrayList<>();
	ArrayList<Integer> bombsy = new ArrayList<>();
	int bombCount = 0;
	private int posY;
	 static class myRandom{
		 static public double  getrandomX(){
			 Random random = new Random();
			 return random.nextDouble();

		 }
		 static public double getrandomY(){
			 Random random = new Random();
			 return random.nextDouble();

		 }
	 }


	private class Coin{
		Texture image;
		public Coin(String path){
			image = new Texture(path);
		}
		public Texture getCoin(){
			return image;
		}

	}
	private class Bomb{
		Texture image;
		public Bomb(String path){
			image = new Texture(path);

		}
		public Texture getBomb(){
			return image;
		}

	}

	private class Character{
		private Texture[] character_states;

		public Character(){
			character_states = new Texture[5];
			character_states[0] = new Texture("run1.png");
			character_states[1] = new Texture("run2.png");
			character_states[2] = new Texture("frame-3.png");
			character_states[3] = new Texture("frame-4.png");
			character_states[4] = new Texture("dizzy.png");

		}

		public Texture get_character_state(int index){
			return character_states[index];
		}


	}
	private Coin coin ;
	 private Bomb bomb;
	 private CollisonObject character_rectangle;

	void setbombscoins(int method){
		float height = (float) myRandom.getrandomY() * Gdx.graphics.getHeight();
		if(method == 0) {
			//float height = (float) myRandom.getrandomY() * Gdx.graphics.getHeight();
			coinys.add((int) height);
			coinxs.add(Gdx.graphics.getWidth() );
		}else if(method == 1){
			bombsy.add((int)height);
			bombsx.add(Gdx.graphics.getWidth());
		}

	}

	private class Background{
		private Texture background;

		public Background(String img  ){
			background =  new Texture(img);

		}
		public void setBackground(Texture t){
			background = t;
		}
		public Texture getbackground(){
			return background;
		}

	}

	private Background background;
	private Character character;
	private CollisonObject bombsrectangle;
	private CollisonObject coinrectangle;

	BitmapFont startupfont;
	BitmapFont touchtoplayfont;
	BitmapFont highscorefont;
	private int highscore;
	private Sound bombsound;
	private Sound rewardsound;
	private int soundcounter = 0;
	private Music music;
	@Override
	public void create () {
		bombsound = Gdx.audio.newSound(Gdx.files.internal("bombexplode.mp3"));
		rewardsound = Gdx.audio.newSound(Gdx.files.internal("reward.mp3"));
		music = Gdx.audio.newMusic(Gdx.files.internal("backgroundsound.mp3"));



		batch = new SpriteBatch();
		startupfont = new BitmapFont();
		touchtoplayfont =  new BitmapFont();
		highscorefont = new BitmapFont();
		startupfont.setColor(Color.GREEN);
		startupfont.getData().setScale(9);
		touchtoplayfont.setColor(Color.WHITE);
		touchtoplayfont.getData().setScale(8);
		highscorefont.setColor(Color.YELLOW);
		highscorefont.getData().setScale(6);
		preferences = Gdx.app.getPreferences("highscores");
		highscore = preferences.getInteger("high" , -1);


		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);


		background = new Background("pethero-bg1.gif");
		character = new Character();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		posY =  height/10;
		coin = new Coin("coin.png");
		bomb = new Bomb("bomb.png");
		bombsrectangle = new CollisonObject();
		coinrectangle = new CollisonObject();
		coinvelocity = 4;
		//(float)((width/2)-250 ), (float)posY ,(float)(width/5) , (float)(height/5)
		int x = (width/2)-250;
		int y = (int)posY;
		int xwidth = width/5;
		int yheight = height/5;

		character_rectangle = new CollisonObject(x , y , xwidth , yheight);


	}
	@Override
	public void render () {

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		if(gamestate == 0) {
			if (Gdx.input.justTouched()) {
				gamestate = 1;
			}
			batch.draw(background.getbackground(), k * (-1), 0, (float) (width * 2), (float) (height));

			startupfont.draw(batch, "GREEDY NINJA", 10, (height / 2) - 30);
			touchtoplayfont.draw(batch, "TOUCH TO PLAY", width / 4, height / 5);

			if (j % 200 == 0) {
				touchtoplayfont.setColor(Color.CORAL);

			} else {
				touchtoplayfont.setColor(Color.CORAL);

			}
			if (highscore == -1) {
				highscore = 0;
			}
			highscorefont.draw(batch, "HIghscore : " + Integer.toString(highscore), 2, height - 40);
		}else if (gamestate == 1) {
			music.play();
			music.setLooping(true);
			if (Gdx.input.isTouched()) {
				//velocity  += gravity;
				posY += 10;

			}
			if (posY > (height / 10) && posY < (height / 5)) {
				posY -= 3;
			} else if (posY >= (height / 5)) {
				posY -= 4;
			} else if (posY == height / 10) {
				posY -= 1;
				//velocity= 0.2f;
			}
			character_rectangle.setY(posY);
			if (direction_background == true) {
				k++;
				if (k == width) {
					direction_background = false;
				}


			} else {
				k--;
				if (k == 100) {
					direction_background = true;
				}

			}
			batch.draw(background.getbackground(), k * (-1), 0, (float) (width * 2), (float) (height));


			batch.draw(character.get_character_state(i), (float) ((width / 2) - 250), (float) posY, (float) (width / 5), (float) (height / 5));
			if (coinCount < 100) {
				coinCount++;
			} else {
				coinCount = 0;
				setbombscoins(0);
			}
			if (bombCount < 200) {
				bombCount++;
			} else {
				bombCount = 0;
				setbombscoins(1);
			}

			int coinrandv = (int)(myRandom.getrandomX() * 3);

			for (int i = 0; i < coinxs.size(); i++) {
				batch.draw(coin.getCoin(), coinxs.get(i), coinys.get(i), 80, 80);
				coinxs.set(i, coinxs.get(i) - coinvelocity);
				coinrectangle.setX(coinxs.get(i));
				coinrectangle.setY(coinys.get(i));
				coinrectangle.setHeight(80);
				coinrectangle.setWidth(80);
				coinrectangle.settype(0);
				coinrectangle.make_rectangle();
				if (character_rectangle.check_collision(coinrectangle) == 1) {
					rewardsound.play();

					coinxs.remove(i);
					coinys.remove(i);
					score++;

				}


			}


			int bombrandv = (int) (myRandom.getrandomY()  * 10);
			for (int i = 0; i < bombsx.size(); i++) {
				batch.draw(bomb.getBomb(), bombsx.get(i), bombsy.get(i), 120, 120);
				bombsx.set(i, bombsx.get(i) - coinvelocity - bombrandv);
				bombsrectangle.settype(1);
				bombsrectangle.setWidth(120);
				bombsrectangle.setHeight(120);
				bombsrectangle.setX(bombsx.get(i));
				bombsrectangle.setY(bombsy.get(i));
				bombsrectangle.make_rectangle();
				if (character_rectangle.check_collision(bombsrectangle) == 1) {
					//gamestate
					music.stop();
					gamestate = 2;
					bombsound.play();

				}
			}
			j++;


			if (j % 4 == 0) {
				i++;
			}
			if (i == 4) {
				i = 0;
			}
			font.draw(batch ,Integer.toString(score) , 100 , height - 40 );

		} else {

//					int high = preferences.getInteger("high" , -1);
//					if(high == -1){
//						preferences.putString("high" , Integer.toString(score));
//
//					}else{
//						if(score > high){
//							preferences.putInteger("high" , Integer.toString(score));
//						}
//
//
//
//				}

			end_pause_time++;

			if (end_pause_time <= 200) {
				background.setBackground(new Texture("explosion.png"));

				batch.draw(background.getbackground(), 0, 0, width, height);
				batch.draw(character.get_character_state(4), (float) width / 4, (float) height / 4, (float) width / 5, (float) height / 5);


			} else {

				background.setBackground(new Texture("pethero-bg1.gif"));
				batch.draw(background.getbackground(), 0, 0, width, height);

				font.draw(batch , "tap to play again" , width/4 , height/2);

				batch.draw(character.get_character_state(4), (float) width / 4, (float) height / 4, (float) width / 5, (float) height / 5);
				if(Gdx.input.justTouched()){
					coinxs.clear();
					coinys.clear();
					bombsy.clear();
					bombsx.clear();
					coinCount = 0;
					bombCount = 0;
					end_pause_time = 0;
					i = 0 ; j =0; k =0 ;
					score = 0;
					gamestate = 1;
				}

			}


		}

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		background = null;
		character = null;
	}
}
