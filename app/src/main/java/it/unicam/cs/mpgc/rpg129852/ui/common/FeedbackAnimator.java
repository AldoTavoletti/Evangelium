package it.unicam.cs.mpgc.rpg129852.ui.common;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * A class used to animate the feedback label shown over the progress bar during levels.
 */
public class FeedbackAnimator {

    private static final double FADE_DURATION_SECONDS = 0.5;
    private static final double PAUSE_DURATION_SECONDS = 1.0;

    private final Label feedbackLabel;
    private SequentialTransition currentAnimation;

    /**
     * Constructs a FeedbackAnimator object.
     *
     * @param feedbackLabel the label to animate
     */
    public FeedbackAnimator(Label feedbackLabel) {
        this.feedbackLabel = feedbackLabel;
    }

    /**
     * Plays the animation.
     *
     * @param message               the text to show inside the feedback label
     * @param onFinishedCallback    the function to run at the end of the animation (can be null)
     */
    public void playFeedback(String message, Runnable onFinishedCallback) {
        if (feedbackLabel == null) {
            if (onFinishedCallback != null) onFinishedCallback.run();
            return;
        }

        abortRunningAnimation();

        feedbackLabel.setText(message);
        feedbackLabel.setVisible(true);

        currentAnimation = createFadeInOutAnimation();
        currentAnimation.setOnFinished(e -> {
            feedbackLabel.setVisible(false);
            if (onFinishedCallback != null) onFinishedCallback.run();
        });

        currentAnimation.play();
    }

    private void abortRunningAnimation() {
        boolean isAnimationRunning = currentAnimation != null && currentAnimation.getStatus() == SequentialTransition.Status.RUNNING;

        if (isAnimationRunning) {
            currentAnimation.stop();
        }
    }

    private SequentialTransition createFadeInOutAnimation() {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(FADE_DURATION_SECONDS), feedbackLabel);
        fadeIn.setFromValue(feedbackLabel.getOpacity());
        fadeIn.setToValue(1.0);

        PauseTransition pause = new PauseTransition(Duration.seconds(PAUSE_DURATION_SECONDS));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(FADE_DURATION_SECONDS), feedbackLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        return new SequentialTransition(fadeIn, pause, fadeOut);
    }
}