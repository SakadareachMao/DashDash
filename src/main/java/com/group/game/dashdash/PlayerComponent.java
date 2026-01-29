package com.group.game.dashdash;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {

    // x = horizontal speed, y = vertical velocity
    private Vec2 velocity = new Vec2(200, 0);
    private final double GRAVITY = 600;

    @Override
    public void onUpdate(double tpf) {
        // 1. Apply Gravity to vertical velocity
        velocity.y += GRAVITY * tpf;

        // 2. Cap falling speed (prevents passing through objects)
        if (velocity.y > 500) velocity.y = 500;

        // 3. Move entity (Velocity * TPF makes it frame-rate independent)
        entity.translate(velocity.x * tpf, velocity.y * tpf);

        // 4. Check floor bounds
        if (entity.getBottomY() > getAppHeight()) {
            // Fix: Syntax for Java-based type casting in FXGL
            FXGL.<HelloApplication>getAppCast().requestNewGame();
        }

        // 5. Keep the bird from flying off the top of the screen
        if (entity.getY() < 0) {
            entity.setY(0);
            velocity.y = 0;
        }
    }

    public void jump() {
        // Setting the velocity directly makes the jump feel "snappy"
        velocity.y = -350;

        // Ensure jump.wav is in src/main/resources/assets/sounds/
        play("jump.wav");
    }
}