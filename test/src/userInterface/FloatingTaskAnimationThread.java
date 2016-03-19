package userInterface;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FloatingTaskAnimationThread extends Service {

	UserInterfaceController ui;
	private boolean isAdded;
	private boolean isDoneAnimating;
	private static final int ANIMATE_SPEED_TOTAL = 800;
	private static final int ANIMATION_DELAY = 5;
	private static final int TIME_INTERVAL_FOR_NEXT_FLOATING_TASK = 10000;

	private double _percentageDone;

	public FloatingTaskAnimationThread(UserInterfaceController userInterfaceController) {
		ui = userInterfaceController;
		_percentageDone = 0;
	}

	public void reset() {
		isAdded = false;
		isDoneAnimating = false;
		_percentageDone = 0.0;
	}

	@Override
	protected Task createTask() {
		return new Task<String>() {
			@Override
			protected String call() throws InterruptedException {
				while (true) {
					Thread.sleep(TIME_INTERVAL_FOR_NEXT_FLOATING_TASK);
					reset();
					long timeStart = System.currentTimeMillis();
					while (!isDoneAnimating) {
						long timePast = System.currentTimeMillis() - timeStart;
						Thread.sleep(300);
						_percentageDone = timePast / (double)ANIMATE_SPEED_TOTAL;
						Platform.runLater(new Runnable() {
							public void run() {
								if (!isAdded) {
									ui.addRandomTaskToDisplay();
									isAdded = true;
								}
								isDoneAnimating = ui.updateFloatingBar(_percentageDone);
								if (_percentageDone > 1) {
									isDoneAnimating = true;
								}
							}
						});
						Thread.sleep(ANIMATION_DELAY);
					}
				}
			}
		};
	}
}