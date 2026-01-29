package com.group.game.dashdash;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class Floor extends Component {

    private double lastFloorX = 0;
    private final double FLOOR_HEIGHT = 50;

    @Override
    public void onUpdate(double tpf) {
        // If the end of our last floor piece is getting close to the screen edge
        if (lastFloorX - entity.getX() < FXGL.getAppWidth()) {
            buildFloor();
        }
    }

    private Rectangle floorView(double width, double height) {
        Rectangle rect = new Rectangle(width, height);
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        // Bind to stageColor so it changes with the theme
        rect.fillProperty().bind(FXGL.getWorldProperties().objectProperty("stageColor"));
        return rect;
    }

    private void buildFloor() {
        double screenWidth = FXGL.getAppWidth();
        double screenHeight = FXGL.getAppHeight();

        // Spawn 5 large floor segments at a time
        for (int i = 0; i < 5; i++) {
            entityBuilder()
                    .at(lastFloorX, screenHeight - FLOOR_HEIGHT)
                    .type(EntityType.FLOOR) // Keep as WALL so your collision logic still works
                    .viewWithBBox(floorView(screenWidth, FLOOR_HEIGHT))
                    .collidable() // Short for .with(new CollidableComponent(true))
                    .buildAndAttach();

            lastFloorX += screenWidth;
        }
    }
}