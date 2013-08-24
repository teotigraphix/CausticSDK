
package com.teotigraphix.libgdx.scene2d;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleEffectActor extends Actor {

    ParticleEffect effect;

    public ParticleEffectActor(ParticleEffect effect) {
        this.effect = effect;
    }

    public void draw(SpriteBatch batch, float parentAlpha) {
        effect.draw(batch); //define behavior when stage calls Actor.draw()
    }

    public void act(float delta) {
        super.act(delta);
        effect.setPosition(getX(), getY()); //set to whatever x/y you prefer
        effect.update(delta); //update it
        effect.start(); //need to start the particle spawning
    }

    public ParticleEffect getEffect() {
        return effect;
    }

}
