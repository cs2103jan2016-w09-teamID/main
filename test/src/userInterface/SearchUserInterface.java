package userInterface;

import java.util.ArrayList;

import entity.TaskEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import mainLogic.Utils;

public class SearchUserInterface implements ViewInterface {

	private static SearchUserInterface _myInstance;

	private Stage _stage;
	private int _stageWidth;
	private int _stageHeight;
	private int _windowPosX;
	private int _windowPosY;

	// font
	static final int FONT_SIZE_LABEL = 16;
	static final int FONT_SIZE_LABEL_DATE = 10;
	static final int FONT_SIZE_TASK = 12;
	static final int FONT_SIZE_INDEX = 8;
	private static final Font FONT_LABEL = new Font(PrimaryUserInterface.DEFAULT_FONT, FONT_SIZE_LABEL);
	private static final Font FONT_TASK = new Font(PrimaryUserInterface.DEFAULT_FONT, FONT_SIZE_TASK);
	private static final Font FONT_INDEX = new Font(PrimaryUserInterface.DEFAULT_FONT, FONT_SIZE_INDEX);
	private static final Font FONT_LABEL_DATE = new Font(PrimaryUserInterface.DEFAULT_FONT, FONT_SIZE_LABEL_DATE);

	static final int LABEL_TITLE_HEIGHT = 35;
	static final int LABEL_TASK_HEIGHT = 30;
	private static final int THRESHOLD = 20;

	private StackPane _mainVbox;
	private VBox _secondaryVbox;

	// variables to control items in floatingView.
	private int _startIndex = -1;
	private int _endIndex = -1;
	private int _selectedIndex = -1;
	private double transLationY = 0;

	private ArrayList<TaskEntity> _searchList;
	private ArrayList<HBox> _searchBoxes = new ArrayList<HBox>();

	public static SearchUserInterface getInstance(Stage primaryStage, Rectangle2D screenBounds, boolean fixedSize) {
		if (_myInstance == null) {
			if (primaryStage == null || screenBounds == null) {
				return null;
			}
			_myInstance = new SearchUserInterface(primaryStage, screenBounds, fixedSize);
			return _myInstance;
		}
		return null;
	}

	private SearchUserInterface(Stage primaryStage, Rectangle2D screenBounds, boolean fixedSize) {
		initializeVaribles(screenBounds, fixedSize);
		initializeStage(primaryStage, _windowPosX, _windowPosY, _stageWidth, _stageHeight);
		buildComponent();
	}

	public void initializeVaribles(Rectangle2D screenBounds, boolean fixedSize) {
		if (fixedSize) {
			_stageWidth = (int) screenBounds.getWidth();
			_stageHeight = (int) (screenBounds.getHeight() - PrimaryUserInterface.COMMAND_BAR_HEIGTH
					- PrimaryUserInterface.COMMAND_BAR_TOP_MARGIN - PrimaryUserInterface.COMMAND_BAR_BOTTOM_MARGIN
					- FloatingBarViewUserInterface.COMPONENT_HEIGHT
					- FloatingBarViewUserInterface.COMPONENT_BOTTOM_MARGIN);
			_windowPosX = DescriptionComponent.CONPONENT_WIDTH + DescriptionComponent.CONPONENT_RIGHT_MARGIN;
			_windowPosY = (int) screenBounds.getHeight() - _stageHeight - PrimaryUserInterface.COMMAND_BAR_HEIGTH
					- PrimaryUserInterface.COMMAND_BAR_TOP_MARGIN - PrimaryUserInterface.COMMAND_BAR_BOTTOM_MARGIN;
		} else {
			_stageWidth = (int) (screenBounds.getWidth() * PrimaryUserInterface.PREFERED_WINDOW_SCALE);
			_stageHeight = (int) (screenBounds.getHeight() - PrimaryUserInterface.COMMAND_BAR_HEIGTH
					- PrimaryUserInterface.COMMAND_BAR_TOP_MARGIN - PrimaryUserInterface.COMMAND_BAR_BOTTOM_MARGIN
					- FloatingBarViewUserInterface.COMPONENT_HEIGHT - FloatingBarViewUserInterface.COMPONENT_TOP_MARGIN
					- FloatingBarViewUserInterface.COMPONENT_BOTTOM_MARGIN);
			_windowPosX = (int) (screenBounds.getWidth()
					- (screenBounds.getWidth() * PrimaryUserInterface.PREFERED_WINDOW_SCALE)) / 2;
			_windowPosY = (int) screenBounds.getHeight() - _stageHeight - PrimaryUserInterface.COMMAND_BAR_HEIGTH
					- PrimaryUserInterface.COMMAND_BAR_TOP_MARGIN - PrimaryUserInterface.COMMAND_BAR_BOTTOM_MARGIN;
		}
	}

	public void initializeStage(Window owner, int applicationX, int applicationY, int stageWidth, int stageHeight) {
		_stage = new Stage();
		_stage.initOwner(owner);
		_stage.initStyle(StageStyle.TRANSPARENT);
		_stage.setX(applicationX);
		_stage.setY(applicationY);

		_mainVbox = new StackPane();
		_mainVbox.setPrefSize(stageWidth, stageHeight);
		_mainVbox.getStylesheets().add(PrimaryUserInterface.STYLE_SHEET);
		_mainVbox.setId("cssRoot");

		Scene s = new Scene(_mainVbox, stageWidth, stageHeight);
		s.setFill(Color.TRANSPARENT);
		_stage.setScene(s);
	}

	private void buildComponent() {
		_mainVbox.getChildren().clear();
		_secondaryVbox = new VBox();
		_secondaryVbox.setMinHeight(_stageHeight - LABEL_TITLE_HEIGHT);
		_secondaryVbox.setMaxHeight(_stageHeight - LABEL_TITLE_HEIGHT);
		_secondaryVbox.setId("cssFloatingViewSecondaryBox");

		_mainVbox.getChildren().add(_secondaryVbox);
		HBox labelTitle = buildTilteLabel();
		_mainVbox.getChildren().add(labelTitle);
		StackPane.setAlignment(labelTitle, Pos.TOP_LEFT);
		StackPane.setAlignment(_secondaryVbox, Pos.TOP_LEFT);
	}

	public void buildContent(ArrayList<TaskEntity> searchList) {
		_searchList = searchList;
		_searchBoxes = new ArrayList<HBox>();
		// when there are no floating task yet
		if (_searchList == null || _searchList.size() == 0) {
			buildHelpWithSearch();
		} else {
			buildSearchList(_searchList);
		}
	}

	private void buildSearchList(ArrayList<TaskEntity> searchList) {
		_secondaryVbox.getChildren().clear();
		_selectedIndex = 0;
		_startIndex = 0;
		if (searchList.size() < THRESHOLD * 2) {
			_endIndex = searchList.size() - 1;
		} else {
			_endIndex = THRESHOLD * 2;
		}

		for (int i = _startIndex; i <= _endIndex; i++) {
			HBox item = buildIndividualSearchItem(searchList.get(i), i);
			_secondaryVbox.getChildren().add(item);
			_searchBoxes.add(item);
		}
		setSelected(0);
	}

	private void buildHelpWithSearch() {
		_secondaryVbox.getChildren().clear();
		Label helpLabel = new Label("Start searching by typing search in the command bar");
		helpLabel.setMinWidth(_stageWidth);
		helpLabel.setMinHeight(_stageHeight - LABEL_TITLE_HEIGHT);
		helpLabel.setAlignment(Pos.CENTER);
		_secondaryVbox.getChildren().add(helpLabel);
	}

	private HBox buildIndividualSearchItem(TaskEntity task, int index) {
		HBox parentBox = new HBox();

		VBox parentBoxChild = new VBox();
		parentBoxChild.setMinWidth(_stageWidth);
		parentBoxChild.setMaxWidth(_stageWidth);

		HBox top = new HBox();
		top.setMinWidth(_stageWidth);
		top.setMaxWidth(_stageWidth);

		Label indexLabel = new Label(Utils.convertDecToBase36(index));
		// Label indexLabel = new Label(Integer.toString(index));
		indexLabel.setMinHeight(LABEL_TASK_HEIGHT);
		indexLabel.setMinWidth(50);
		indexLabel.setAlignment(Pos.CENTER);
		indexLabel.setFont(Font.font(PrimaryUserInterface.DEFAULT_FONT, FontWeight.BOLD, FONT_SIZE_TASK));
		top.getChildren().add(indexLabel);

		Label timeLabel = new Label();
		timeLabel.setText(task.getTime());
		timeLabel.setMinHeight(LABEL_TASK_HEIGHT);
		timeLabel.setAlignment(Pos.CENTER);
		timeLabel.setFont(FONT_TASK);
		top.getChildren().add(timeLabel);

		Label nameLabel = new Label();
		nameLabel.setText(task.getName());
		nameLabel.setMinHeight(LABEL_TASK_HEIGHT);
		nameLabel.setAlignment(Pos.CENTER);
		nameLabel.setFont(FONT_TASK);
		HBox.setMargin(nameLabel, new Insets(0, 10, 0, 10));
		top.getChildren().add(nameLabel);

		HBox mid = new HBox();
		Label indexPlaceHolder = new Label();
		indexPlaceHolder.setMinWidth(50);
		mid.getChildren().add(indexPlaceHolder);

		Text description = new Text();
		description.setText(task.getDescription());
		description.setWrappingWidth(_stageWidth - 50);
		mid.getChildren().add(description);
		mid.setMaxHeight(description.getBoundsInLocal().getHeight()+10);

		parentBoxChild.getChildren().add(top);
		parentBoxChild.getChildren().add(mid);
		parentBox.getChildren().add(parentBoxChild);
		return parentBox;
	}

	public HBox buildTilteLabel() {
		HBox titleLableBox = new HBox();
		titleLableBox.setId("cssSearchTitle");
		titleLableBox.setMinWidth(_stageWidth);
		titleLableBox.setMinHeight(LABEL_TITLE_HEIGHT);
		titleLableBox.setMaxHeight(LABEL_TITLE_HEIGHT);

		Label searchTitle = new Label("Search View");
		searchTitle.setMinWidth(_stageWidth);
		searchTitle.setFont(FONT_LABEL);
		searchTitle.setMinHeight(LABEL_TITLE_HEIGHT);
		searchTitle.setMaxHeight(LABEL_TITLE_HEIGHT);
		HBox.setMargin(searchTitle, new Insets(0, 0, 0, 30));

		titleLableBox.getChildren().add(searchTitle);
		return titleLableBox;
	}

	public void update(int value) {
		if (value > 0)// ctrl down
		{
			if (_endIndex + 1 < _searchList.size()) {
				if (_selectedIndex - _startIndex >= THRESHOLD) {
					removeFirstTask();
					addLastItem();
				}
			}
		} else if (value < 0) {
			if (_startIndex > 0) {
				if (_endIndex - _selectedIndex >= THRESHOLD) {
					removeLastTask();
					addFirstItem();
				}
			}
		}
	}

	private void addFirstItem() {
		_startIndex--;
		HBox item = buildIndividualSearchItem(_searchList.get(_startIndex), _startIndex);
		_searchBoxes.add(0, item);
		_secondaryVbox.getChildren().add(0, item);
	}

	private void removeLastTask() {
		_endIndex--;
		HBox itemToRemove = _searchBoxes.remove(_searchBoxes.size() - 1);
		_secondaryVbox.getChildren().remove(itemToRemove);
	}

	private void addLastItem() {
		_endIndex++;
		HBox item = buildIndividualSearchItem(_searchList.get(_endIndex), _endIndex);
		_searchBoxes.add(item);
		_secondaryVbox.getChildren().add(item);
	}

	private void removeFirstTask() {
		_startIndex++;
		HBox item = _searchBoxes.remove(0);
		_secondaryVbox.getChildren().remove(item);
	}

	public void setSelected(int value) {
		int temp = _selectedIndex + value;
		if (isBetweenRange(temp)) {
			HBox prevItem = _searchBoxes.get(_selectedIndex - _startIndex);
			prevItem.setId("");
			_selectedIndex = temp;
			HBox item = _searchBoxes.get(_selectedIndex - _startIndex);
			item.setId("cssSearchSelected");
			translateY(getTopHeight(_selectedIndex - _startIndex));
		}
	}

	public double getTopHeight(int index) {
		double posY = 0;
		for (int i = 0; i <= index; i++) {
			posY += _searchBoxes.get(i).getHeight();
		}
		return posY;
	}

	public void translateY(double itemHeight) {
		int entireAreaHeight = _stageHeight - LABEL_TITLE_HEIGHT;
		double posY = LABEL_TITLE_HEIGHT;
		if (itemHeight < entireAreaHeight) {

		} else {
			posY += (itemHeight - entireAreaHeight);
		}
		_secondaryVbox.setTranslateY(-posY);
	}

	public boolean isBetweenRange(int index) {
		if (index >= _startIndex && index <= _endIndex) {
			return true;
		}
		return false;
	}

	public void show() {
		_stage.show();
	}

	public void hide() {
		_stage.hide();
	}

	public void updateTranslateY(double posY) {
		// TODO Auto-generated method stub

	}

}
