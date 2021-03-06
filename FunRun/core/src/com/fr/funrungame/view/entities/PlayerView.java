package com.fr.funrungame.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fr.funrungame.FunRunGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.fr.funrungame.model.entities.EntityModel;
import com.fr.funrungame.model.entities.PlayerModel;

/**
 * A view representing a player
 */
public class PlayerView extends EntityView {

    /**
     * The time between the animation frames
     */
    private static final float FRAME_TIME = 0.09f;

    private PlayerModel.State state;
    private PlayerModel.Boost boost;

    /**
     * The animation used when the player is running
     */
    private Animation<TextureRegion> runningAnimation;

    /**
     * The texture used when the player is not running
     */
    private TextureRegion notRunningRegion;

    /**
     * The texture used when the player is jumping
     */
    private TextureRegion jumpingRegion;

    /**
     * The texture used when the player is falling
     */
    private TextureRegion fallingRegion;

    /**
     * The texture used when the player is shielded
     */
    private TextureRegion shieldRegion;

    /**
     * The animation used when the player is running with a shield
     */
    private Animation<TextureRegion> runningShieldedAnimation;

    /**
     * The texture used when the player is jumping with a shield
     */
    private TextureRegion jumpingShieldedRegion;

    /**
     * The texture used when the player is falling with a shield
     */
    private TextureRegion fallingShieldedRegion;

    /**
     * Time since the player started the game. Used
     * to calculate the frame to show in animations.
     */
    private float stateTime;

    /**
     * Is the player running.
     */
    private boolean running;

    /**
     * Is the player jumping.
     */
    private boolean jumping;

    /**
     * Is the player falling.
     */
    private boolean falling;

    /**
     * Is the player invulnerable
     */
    private boolean invulnerable;

    /**
     * Is the player dead
     */
    private boolean dead;

    /**
     * Is the player shielded
     */
    private boolean shield;

    /**
     * Sprite alpha modulation
     */
    private float alpha = 1f;

    /**
     * Sprite alpha modulation when player is dead
     */
    private float dead_alpha = .0f;

    /**
     * PLayer View Constructor.
     * It initializes all the needed elements.
     *
     * @param game The current game session.
     */
    public PlayerView(FunRunGame game){
        super(game);
        stateTime = 0;
    }

    /**
     * Ghost View Constructor.
     * It initializes all the needed elements.
     *
     * @param game The current game session.
     * @param alpha alpha modulation
     */
    public PlayerView(FunRunGame game, float alpha) {
        this(game);
        this.alpha = alpha;
    }

    /**
     * Creates all the sprites of the player.
     *
     * @param game The current game session.
     */
    public Sprite createSprite(FunRunGame game) {
        runningAnimation = createRunningAnimation(game);
        notRunningRegion = createNotRunningRegion(game);
        jumpingRegion = createJumpingRegion(game);
        fallingRegion = createFallingRegion(game);
        shieldRegion = createShieldRegion(game);
        runningShieldedAnimation = createRunningShieldedAnimation(game);
        jumpingShieldedRegion = createJumpingShieldedRegion(game);
        fallingShieldedRegion = createFallingShieldedRegion(game);

        return new Sprite(notRunningRegion);
    }

    /**
     * Creates the sprite of the player when he is not running.
     *
     * @param game The current game session.
     */
    private TextureRegion createNotRunningRegion(FunRunGame game){
        Texture texture = game.getAssetManager().get("player.png");
        return new TextureRegion(texture, texture.getWidth(), texture.getHeight());
    }

    /**
     * Creates the sprite of the player when he is jumping.
     *
     * @param game The current game session.
     */
    private TextureRegion createJumpingRegion(FunRunGame game){
        Texture texture = game.getAssetManager().get("player_jumping.png");
        return new TextureRegion(texture, texture.getWidth(), texture.getHeight());
    }

    /**
     * Creates the sprite of the player when he is falling.
     *
     * @param game The current game session.
     */
    private TextureRegion createFallingRegion(FunRunGame game){
        Texture texture = game.getAssetManager().get("player_falling.png");
        return new TextureRegion(texture, texture.getWidth(), texture.getHeight());
    }

    /**
     * Creates the sprite of the player when he has a shield.
     *
     * @param game The current game session.
     */
    private TextureRegion createShieldRegion(FunRunGame game){
        Texture texture = game.getAssetManager().get("player_shielded.png");
        return new TextureRegion(texture, texture.getWidth(), texture.getHeight());
    }

    /**
     * Creates the sprite of the player when he is running.
     *
     * @param game The current game session.
     */
    private Animation<TextureRegion> createRunningAnimation(FunRunGame game) {
        Texture thrustTexture = game.getAssetManager().get("player_running.png");
        TextureRegion[][] thrustRegion = TextureRegion.split(thrustTexture, thrustTexture.getWidth() / 6, thrustTexture.getHeight());

        TextureRegion[] frames = new TextureRegion[6];
        System.arraycopy(thrustRegion[0], 0, frames, 0, 6);

        return new Animation<TextureRegion>(FRAME_TIME, frames);
    }

    /**
     * Creates the sprite of the player when he is falling with a shield.
     *
     * @param game The current game session.
     */
    private TextureRegion createFallingShieldedRegion(FunRunGame game) {
        Texture texture = game.getAssetManager().get("player_falling_shielded.png");
        return new TextureRegion(texture, texture.getWidth(), texture.getHeight());
    }

    /**
     * Creates the sprite of the player when he is jumping with a shield.
     *
     * @param game The current game session.
     */
    private TextureRegion createJumpingShieldedRegion(FunRunGame game) {
        Texture texture = game.getAssetManager().get("player_jumping_shielded.png");
        return new TextureRegion(texture, texture.getWidth(), texture.getHeight());
    }

    /**
     * Creates the sprite of the player when he is running with a shield.
     *
     * @param game The current game session.
     */
    private Animation<TextureRegion> createRunningShieldedAnimation(FunRunGame game) {
        Texture thrustTexture = game.getAssetManager().get("player_running_shielded.png");
        TextureRegion[][] thrustRegion = TextureRegion.split(thrustTexture, thrustTexture.getWidth() / 6, thrustTexture.getHeight());

        TextureRegion[] frames = new TextureRegion[6];
        System.arraycopy(thrustRegion[0], 0, frames, 0, 6);

        return new Animation<TextureRegion>(FRAME_TIME, frames);
    }

    /**
     * Updates the player current states.
     *
     * @param model player model.
     */
    @Override
    public void update(EntityModel model) {
        super.update(model);

        state = ((PlayerModel)model).getState();
        boost = ((PlayerModel)model).getBoost();
    }

    /**
     * Draws the player view.
     *
     * @param batch game batch.
     */
    @Override
    public void draw(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        if (boost == PlayerModel.Boost.SHIELD) {
            switch(state) {
                case JUMPING:
                    sprite.setRegion(jumpingShieldedRegion);
                    break;
                case FALLING:
                    sprite.setRegion(fallingShieldedRegion);
                    break;
                case RUNNING:
                    sprite.setRegion(runningShieldedAnimation.getKeyFrame(stateTime, true));
                    break;
                case DEFAULT:
                    sprite.setRegion(shieldRegion);
                    break;
            }

        } else {
            switch(state) {
                case JUMPING:
                    sprite.setRegion(jumpingRegion);
                    break;
                case FALLING:
                    sprite.setRegion(fallingRegion);
                    break;
                case RUNNING:
                    sprite.setRegion(runningAnimation.getKeyFrame(stateTime, true));
                    break;
                case DEFAULT:
                    sprite.setRegion(notRunningRegion);
                    break;
            }
        }

        if (state == PlayerModel.State.DEAD) {
            sprite.draw(batch, 0.1f * alpha);
        } else if (boost == PlayerModel.Boost.INVULNERABLE) {
            sprite.draw(batch, dead_alpha * alpha);
            dead_alpha += 0.05;
            if (dead_alpha > 1) dead_alpha = 0;
        } else {
            sprite.draw(batch, alpha);
        }
    }
}
