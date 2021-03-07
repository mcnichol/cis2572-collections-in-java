package org.mcnichol.utility;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * <h1>UserInterfaceUtility</h1>
 * Utility interface with default method for constructing a Timeline animation loop with passsed in eventHanlders for updating
 * <p>
 *
 * @author Michael McNichol
 * @version 1.0
 * @since 2021/02/21
 */

public interface UserInterfaceUtility {
    /**
     * Constructs keyframe timing and timeline
     *
     * @param eventEventHandler A Collection of eventHandlers that should be triggered with each event loop
     * @return handle to timeline event loop for play and pause functionality
     */
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
