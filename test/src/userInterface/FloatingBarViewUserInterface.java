//@@author A0125514N
package userInterface;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class FloatingBarViewUserInterface implements ViewInterface {

	static final int COMPONENT_HEIGHT = 50;
	static final int COMPONENT_TOP_MARGIN = 50;
	static final int COMPONENT_BOTTOM_MARGIN = 2;
	private static final int POSITION_ZERO = 0;
	private static final int LEFT_MARGIN = 10;

	private static final int LABEL_TITLE_WIDTH = 250;
	private static final int FONT_SIZE_TITLE_LABEL = 20;
	private static final int FONT_SIZE_TASK = 16;
	private static final Font FONT_LABEL_TITLE = new Font(PrimaryUserInterface.FONT_DEFAULT, FONT_SIZE_TITLE_LABEL);
	private static final Font FONT_LABEL_TASK = new Font(PrimaryUserInterface.FONT_DEFAULT, FONT_SIZE_TASK);

	private static final String CSS_LABEL = "cssLabelsFloatingBar";

	private String _styleSheet;

	private Stage _stage;
	private int _stageWidth;
	private int _stageHeight;
	private int _windowPosX;
	private int _windowPosY;
	private HBox _mainHBox;
	private VBox _mainfloatingTaskArea;

	public FloatingBarViewUserInterface(Stage primaryStage, Rectangle2D screenBounds, boolean fixedSize,
			String styleSheet, EventHandler<MouseEvent> mouseEvent) {
		_styleSheet = styleSheet;
		initializeVaribles(screenBounds, fixedSize);
		initializeStage(primaryStage, _windowPosX, _windowPosY, _stageWidth, _stageHeight,mouseEvent);
	}

	public void initializeVaribles(Rectangle2D screenBounds, boolean fixedSize) {
		if (fixedSize) {
			_stageWidth = (int) screenBounds.getWidth();
			_stageHeight = COMPONENT_HEIGHT;
			_windowPosX = POSITION_ZERO;
			_windowPosY = POSITION_ZERO;
		} else {
			_stageWidth = (int) (screenBounds.getWidth() * PrimaryUserInterface.PREFERED_WINDOW_SCALE);
			_stageHeight = COMPONENT_HEIGHT;
			_windowPosX = (int) (screenBounds.getWidth()
					- (screenBounds.getWidth() * PrimaryUserInterface.PREFERED_WINDOW_SCALE)) / 2;
			_windowPosY = COMPONENT_TOP_MARGIN;
		}
	}

	public void initializeStage(Window owner, int applicationX, int applicationY, int windowWidth, int windowHeight,EventHandler<MouseEvent> mouseEvent) {
		_stage = new Stage();
		_stage.initOwner(owner);
		_stage.initStyle(StageStyle.TRANSPARENT);
		_stage.setX(applicationX);
		_stage.setY(applicationY);

		_mainHBox = new HBox();
		_mainHBox.setPrefHeight(_stageHeight);
		_mainHBox.setMaxHeight(_stageHeight);
		_mainHBox.setId("cssRootFloatingBar");

		Scene scene = new Scene(_mainHBox, windowWidth, windowHeight, Color.TRANSPARENT);
		scene.getStylesheets().add(_styleSheet);
		scene.setFill(Color.TRANSPARENT);
		scene.setOnMousePressed(mouseEvent);
		_stage.setScene(scene);

		build();
	}

	public void build() {
		Label floatTitleLabel = new Label("Floating task of the day");
		floatTitleLabel.getStyleClass().add(CSS_LABEL);
		floatTitleLabel.setMinHeight(_stageHeight);
		floatTitleLabel.setMinWidth(LABEL_TITLE_WIDTH);
		floatTitleLabel.setId("cssFloatingBarTitleLabel");
		floatTitleLabel.setAlignment(Pos.CENTER);
		floatTitleLabel.setFont(FONT_LABEL_TITLE);

		_mainfloatingTaskArea = new VBox();
		_mainfloatingTaskArea.setMinWidth(_stageWidth - LABEL_TITLE_WIDTH);
		_mainfloatingTaskArea.setId("cssFloatingBarContentArea");

		_mainHBox.getChildren().add(floatTitleLabel);
		_mainHBox.getChildren().add(_mainfloatingTaskArea);
	}

	public void addTask(String taskDesc) {
		Label floatTask = new Label(taskDesc);
		floatTask.getStyleClass().add(CSS_LABEL);
		floatTask.setMinHeight(_stageHeight);
		floatTask.setMaxHeight(_stageHeight);
		floatTask.setAlignment(Pos.CENTER);
		floatTask.setFont(FONT_LABEL_TASK);
		VBox.setMargin(floatTask, new Insets(0, 0, 0, LEFT_MARGIN));
		_mainfloatingTaskArea.getChildren().add(floatTask);
	}

	private void removeTopItem() {
		if (_mainfloatingTaskArea.getChildren().size() > 1) {
			_mainfloatingTaskArea.getChildren().remove(0);
			_mainfloatingTaskArea.setTranslateY(0);
		}
	}

	public void clearFloatingBar() {
		_mainfloatingTaskArea.getChildren().clear();
	}

	public void show() {
		_stage.show();
	}

	public void hide() {
		_stage.hide();
	}

	public void update(int value) {

	}

	public void updateTranslateY(double posY) {

	}

	public boolean animateView(double percentageDone) {
		double posY = percentageDone * (double) _stageHeight;
		if (posY < _stageHeight) {
			_mainfloatingTaskArea.setTranslateY(-posY);
			return false;
		} else {
			removeTopItem();
			return true;
		}
	}

	public void destoryStage() {
		_stage.close();
	}

	public void changeTheme(String styleSheet) {
		_stage.getScene().getStylesheets().clear();
		_styleSheet = styleSheet;
		_stage.getScene().getStylesheets().add(styleSheet);		
	}

}
