package userInterface;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class ScrollTaskAnimation extends Task<Integer> {

	private int currentIndex;
	private int indexToGo;
	private int direction;
	private int numberOfMilliSecondsBeforeIncreaseSpeed = 300;
	UserInterfaceController ui;

	public ScrollTaskAnimation(int currentIndex, int indexToGo, UserInterfaceController userInterfaceController) {
		this.currentIndex = currentIndex;
		this.indexToGo = indexToGo;
		ui = userInterfaceController;
	}

	@Override
	protected Integer call() throws Exception {
		direction = 0;
		if (currentIndex < indexToGo) {
			direction = 1;
		} else {
			direction = -1;
		}

		long startTime = System.currentTimeMillis();
		while (currentIndex != indexToGo) {
			startTime = checkTime(startTime);
			Platform.runLater(new Runnable() {
				public void run() {
					ui.update(direction);
					currentIndex = currentIndex + direction;
					checkExceed();
				}
			});
			Thread.sleep(80);
		}

		return 1;
	}

	private long checkTime(long startTime) {
		long currTime = System.currentTimeMillis();
		if (currTime - startTime > numberOfMilliSecondsBeforeIncreaseSpeed) {
			increaseSpeed();
			return currTime;
		}
		return startTime;
	}

	private void increaseSpeed() {
		if (direction < 1) {
			direction--;
		} else {
			direction++;
		}
	}

	public void checkExceed() {
		if (direction < 0) {
			if (currentIndex + direction < indexToGo) {
				currentIndex = indexToGo;
			}
		} else if (direction > 0) {
			if (currentIndex + direction > indexToGo) {
				currentIndex = indexToGo;
			}
		}
	}

}
