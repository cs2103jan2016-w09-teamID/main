package userInterface;

import java.util.ArrayList;
import entity.DescriptionLabel;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class DescriptionComponent implements ViewInterface {

    static final int CONPONENT_WIDTH = 50;
    static final int CONPONENT_RIGHT_MARGIN = 0;
    private static final int POSITION_ZERO = 0;

    private final double LABEL_SIZE_LARGE = 200;
    private final double LABEL_SIZE_MEDIUM = 100;
    private final double LABEL_SIZE_SMALL = 24;

    private Stage _stage;
    private int _stageWidth;
    private int _stageHeight;
    private int _windowPosX;
    private int _windowPosY;

    private GridPane _mainVbox;
    private double _translationY = 0;

    public DescriptionComponent(Stage parentStage, Rectangle2D screenBounds, boolean fixedSize) {
        initializeVaribles(screenBounds, fixedSize);
        initializeStage(parentStage, _windowPosX, _windowPosY, _stageWidth, _stageHeight);
    }

    public void initializeVaribles(Rectangle2D screenBounds, boolean fixedSize) {
        if (fixedSize) {
            _stageWidth = CONPONENT_WIDTH;
            _stageHeight = (int) (screenBounds.getHeight() - PrimaryUserInterface.COMMAND_BAR_HEIGTH
                    - PrimaryUserInterface.COMMAND_BAR_BOTTOM_MARGIN
                    - PrimaryUserInterface.COMMAND_BAR_TOP_MARGIN
                    - FloatingBarViewUserInterface.COMPONENT_HEIGHT
                    - FloatingBarViewUserInterface.COMPONENT_BOTTOM_MARGIN);
            _windowPosX = POSITION_ZERO;
            _windowPosY = (int) screenBounds.getHeight() - _stageHeight
                    - PrimaryUserInterface.COMMAND_BAR_HEIGTH - PrimaryUserInterface.COMMAND_BAR_TOP_MARGIN
                    - PrimaryUserInterface.COMMAND_BAR_BOTTOM_MARGIN;
        } else {
            _stageWidth = CONPONENT_WIDTH;
            _stageHeight = (int) (screenBounds.getHeight() - PrimaryUserInterface.COMMAND_BAR_HEIGTH
                    - PrimaryUserInterface.COMMAND_BAR_TOP_MARGIN
                    - PrimaryUserInterface.COMMAND_BAR_BOTTOM_MARGIN
                    - FloatingBarViewUserInterface.COMPONENT_HEIGHT
                    - FloatingBarViewUserInterface.COMPONENT_TOP_MARGIN
                    - FloatingBarViewUserInterface.COMPONENT_BOTTOM_MARGIN);
            _windowPosX = (int) (screenBounds.getWidth()
                    - (screenBounds.getWidth() * PrimaryUserInterface.PREFERED_WINDOW_SCALE)) / 2;
            _windowPosY = (int) screenBounds.getHeight() - _stageHeight
                    - PrimaryUserInterface.COMMAND_BAR_HEIGTH - PrimaryUserInterface.COMMAND_BAR_TOP_MARGIN
                    - PrimaryUserInterface.COMMAND_BAR_BOTTOM_MARGIN;
        }
    }

    public void initializeStage(Window owner, int applicationX, int applicationY, int windowWidth,
            int windowHeight) {
        _stage = new Stage();
        _stage.initOwner(owner);
        _stage.initStyle(StageStyle.UNDECORATED);
        _stage.setX(applicationX);
        _stage.setY(applicationY);

        _mainVbox = new GridPane();
        _mainVbox.getStylesheets().add(PrimaryUserInterface.STYLE_SHEET);
        _stage.setScene(new Scene(_mainVbox, windowWidth, windowHeight));
    }

    public void initializeContent(ArrayList<DescriptionLabel> descriptionLabels) {
        buildComponent(descriptionLabels, 0);
    }

    public void buildComponent(ArrayList<DescriptionLabel> descriptionLabels, int selectedIndex) {
        _mainVbox.getChildren().clear();
        double totalBuildedHeight = 0;
        for (int i = 0; i < descriptionLabels.size(); i++) {
            _mainVbox.add(buildIndividualLabel(descriptionLabels.get(i), totalBuildedHeight), 0, i);
            totalBuildedHeight += descriptionLabels.get(i).getHeight();
        }
    }

    public VBox buildIndividualLabel(DescriptionLabel dLabel, double totalBuildedHeight) {
        VBox vbox = new VBox();
        vbox.setMinHeight(dLabel.getHeight());
        vbox.setMinWidth(CONPONENT_WIDTH);
        if (dLabel.isSelected()) {
            vbox.setId("cssTaskViewVerticleBoxSelected");
        } else {
            vbox.setId("cssTaskViewVerticleBox");
        }
        double posYStart = _translationY + totalBuildedHeight;
        double posYEnd = posYStart + dLabel.getHeight();

        calculateLabelHeight(posYStart, posYEnd, vbox, dLabel);
        return vbox;
    }

    public void calculateLabelHeight(double posYStart, double posYEnd, VBox vbox, DescriptionLabel dLabel) {
        Label main = new Label();
        Label extra = new Label();
        if ((posYStart >= 0 && posYStart < _stageHeight) || (posYEnd <= _stageHeight && posYEnd > 0)) {
            // partially or fully inside screen
            if (posYStart >= 0 && posYEnd <= _stageHeight) {
                // fully in screen
                main.setMinHeight(posYEnd - posYStart);
                vbox.getChildren().add(buildLabelBaseOnHeight(main, dLabel, posYEnd - posYStart));
            } else if (posYStart < 0) {
                // tail in screen only
                extra.setMinHeight(-posYStart);
                vbox.getChildren().add(extra);
                main.setMinHeight(posYEnd);
                vbox.getChildren().add(buildLabelBaseOnHeight(main, dLabel, posYEnd));
            } else if (posYStart >= 0) {
                // head in screen only
                main.setMinHeight(_stageHeight - posYStart);
                vbox.getChildren().add(buildLabelBaseOnHeight(main, dLabel, _stageHeight - posYStart));
                extra.setMinHeight(posYEnd - _stageHeight);
                vbox.getChildren().add(extra);
            }
        } else if (posYStart <= 0 && posYEnd >= _stageHeight) {
            // body in screen, head or tail or head and tail not in screen
            Label tempLabel = new Label();
            tempLabel.setMinHeight(-posYStart);
            vbox.getChildren().add(tempLabel);
            main.setMinHeight(_stageHeight);
            vbox.getChildren().add(buildLabelBaseOnHeight(main, dLabel, _stageHeight));
            extra.setMinHeight(posYEnd - _stageHeight);
            vbox.getChildren().add(extra);
        }
    }

    public Label buildLabelBaseOnHeight(Label label, DescriptionLabel dLabel, double height) {
        if (height > LABEL_SIZE_MEDIUM) {

            label.setMinHeight(CONPONENT_WIDTH);
            label.setMinWidth(height);
            label.setRotate(270);
            double translationX = -(height / 2) + CONPONENT_WIDTH / 2;
            double translationY = (height / 2) - CONPONENT_WIDTH / 2;
            label.setTranslateX(translationX);
            label.setTranslateY(translationY);
            if (height > LABEL_SIZE_LARGE) {
                label.setText(dLabel.getFullWeekLabel());
            } else {
                label.setText(dLabel.getMediumWeekLabel());
            }

        } else if (height <= LABEL_SIZE_MEDIUM) {
            label.setText(dLabel.getSmallWeekLabel());
            label.setMinWidth(CONPONENT_WIDTH);
        }

        label.setAlignment(Pos.CENTER);
        return label;
    }

    public void updateTranslateY(double value) {
        _translationY = value;
        _mainVbox.setTranslateY(value);
    }

    public void show() {
        _stage.show();
    }

    public void hide() {
        _stage.hide();
    }

    public GridPane getMainVBox() {
        return _mainVbox;
    }

    public void update(int value) {
    }

    public void destoryStage() {
        _stage.close();
    }

}
