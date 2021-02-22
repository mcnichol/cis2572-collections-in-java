package org.mcnichol.utility;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public interface UserInterfaceUtility {
    default Timeline createUpdatingAnimationLoop(EventHandler<ActionEvent> eventEventHandler) {
        Timeline timeline = new Timeline();

        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(.25),
                eventEventHandler
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        return timeline;
    }
}
