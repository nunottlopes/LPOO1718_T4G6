package com.fr.funrungame.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.fr.funrungame.controller.entities.*;
import com.fr.funrungame.model.GameModel;
import com.fr.funrungame.model.entities.*;

import java.util.ArrayList;
import java.util.List;

public class GameController implements ContactListener{

    /**
     * The singleton instance of this controller
     */
    private static GameController instance;

    /**
     * The arena width in meters.
     */
    public static final int GAME_WIDTH = 1080;

    /**
     * The arena height in meters.
     */
    public static final int GAME_HEIGHT = 720;

    /**
     * Accumulator used to calculate the simulation step.
     */
    private float accumulator;

    /**
     * The physics world controlled by this controller.
     */
    private final World world;

    private final PlayerBody playerBody;

    private List<PlatformBody> platformsBody;

    private List<PowerUpBody> powerUps;

    private List<EnemyBody> enemies;

    private EndLineBody endline;

    private float time;


    private GameController() {
        world = new World(new Vector2(0, -9.8f), true);
        time = 0;

        playerBody = new PlayerBody(world, GameModel.getInstance().getPlayers().get(0));

        platformsBody = new ArrayList<PlatformBody>();
        for(int i = 0; i < GameModel.getInstance().getPlatformsModel().size(); i++){
            platformsBody.add(new PlatformBody(world,GameModel.getInstance().getPlatformsModel().get(i), GameModel.getInstance().getPlatformsModel().get(i).getObject()));
        }

        powerUps = new ArrayList<PowerUpBody>();
        for(int i = 0; i < GameModel.getInstance().getPowerUps().size(); i++){
            powerUps.add(new PowerUpBody(world,GameModel.getInstance().getPowerUps().get(i), GameModel.getInstance().getPowerUps().get(i).getObject()));
        }
        enemies = new ArrayList<EnemyBody>();
        for(int i = 0; i < GameModel.getInstance().getEnemies().size(); i++){
            enemies.add(new EnemyBody(world,GameModel.getInstance().getEnemies().get(i), GameModel.getInstance().getEnemies().get(i).getObject()));
        }

        endline = new EndLineBody(world, GameModel.getInstance().getEndline(), GameModel.getInstance().getEndline().getObject());

        world.setContactListener(this);
    }

    /**
     * Returns a singleton instance of a game controller
     *
     * @return the singleton instance
     */
    public static GameController getInstance() {
        if (instance == null)
            instance = new GameController();
        return instance;
    }

    /**
     * Returns the world controlled by this controller. Needed for debugging purposes only.
     *
     * @return The world controlled by this controller.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Calculates the next physics step of duration delta (in seconds).
     *
     * @param delta The size of this physics step in seconds.
     */
    public void update(float delta) {
        time += delta;
        GameModel.getInstance().update(delta);

        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 1/60f) {
            world.step(1/60f, 6, 2);
            accumulator -= 1/60f;
        }

        playerVerifications(delta);

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            ((EntityModel) body.getUserData()).setPosition(body.getPosition().x, body.getPosition().y);
        }
    }

    private void playerVerifications(float delta){
        playerBody.update(delta);

        //to keep the player always moving forward
        if(playerBody.getBody().getLinearVelocity().x <= 5 && !playerBody.isFINISHED() && !playerBody.isDEAD())
            playerBody.run();

        //jumping and fallind handlers
        if(playerBody.getBody().getLinearVelocity().x == 0){
            ((PlayerModel) playerBody.getUserData()).setRunning(false);
        }
        else{
            ((PlayerModel) playerBody.getUserData()).setRunning(true);
        }

        if(playerBody.getBody().getLinearVelocity().y > 0){
            ((PlayerModel) playerBody.getUserData()).setJumping(true);
            ((PlayerModel) playerBody.getUserData()).setFalling(false);
        }
        else if(playerBody.getBody().getLinearVelocity().y < 0){
            ((PlayerModel) playerBody.getUserData()).setJumping(false);
            ((PlayerModel) playerBody.getUserData()).setFalling(true);
        }

        //power up handler
        if(!playerBody.isFINISHED()) {
            if (((PlayerModel) playerBody.getUserData()).getPowerup() != null) {
                if (((PlayerModel) playerBody.getUserData()).getPowerup().update(delta, playerBody) == 1) {
                    ((PlayerModel) playerBody.getUserData()).removePowerup();
                }
            }
        }
    }

    public void jump(){
        if(playerBody.getBody().getLinearVelocity().x == 0 && playerBody.getBody().getLinearVelocity().y < 4)
            playerBody.jump(1);
        else if(playerBody.getBody().getLinearVelocity().y == 0)
            playerBody.jump(0);
    }

    public void usePowerUp(){
        if(((PlayerModel) playerBody.getUserData()).getPowerup() != null){
            ((PlayerModel) playerBody.getUserData()).getPowerup().action();
        }
    }
    public void moveDown() {
        playerBody.moveDown();
    }

    public PlayerBody getPlayerBody() {
        return playerBody;
    }

    /**
     * A contact between two objects was detected
     *
     * @param contact the detected contact
     */
    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (bodyA.getUserData() instanceof PlayerModel && bodyB.getUserData() instanceof PlatformModel){
            ((PlayerModel) playerBody.getUserData()).setJumping(false);
            ((PlayerModel) playerBody.getUserData()).setFalling(false);
        }

        if (bodyA.getUserData() instanceof PlayerModel && bodyB.getUserData() instanceof PowerUpModel){
            ((PowerUpModel) bodyB.getUserData()).givePowerUp((PlayerModel) playerBody.getUserData());
        }

        if (bodyA.getUserData() instanceof PlayerModel && bodyB.getUserData() instanceof EnemyModel){
            playerBody.die();
        }

        if (bodyA.getUserData() instanceof PlayerModel && bodyB.getUserData() instanceof EndLineModel){
            playerBody.setFinish();
            sendToServer(playerBody.getHistory());

        }
    }

    public float getTime() {
        return time;
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void sendToServer(ArrayList history) {
        



    }
}
