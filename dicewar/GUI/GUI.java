package dicewar.GUI;

import dicewar.Engine;
import dicewar.SaveGame;
import dicewar.objects.Filed;
import dicewar.objects.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Optional;


public class GUI extends Application {
    Engine engine;
    Filed filed = null;
    UserButton[][] bufArray = null;
    UserButton warringCell = null;
    Label label;
    String labelText;

    void handleButtonAction(UserButton button) {
        if (warringCell == null) {
            warringCell = button;
            labelText = engine.getFiled().getCurrentPlayer().getName() + ": " + engine.getFiled().getCell(button.getX(), button.getY()).getDices() + " dices";
            label.setText(labelText);
            button.getStyleClass().clear();
            bufArray[button.getX()][button.getY()].getStyleClass().add("player" + engine.getFiled().getCell(button.getX(), button.getY()).getBoss_id() + "-" + engine.getFiled().getCell(button.getX(), button.getY()).getDices() + "-DOWN");
            for (int i = 0; i < engine.getFiled().getFiled().length; i++) {
                for (int j = 0; j < engine.getFiled().getFiled()[0].length; j++) {
                    if ((i == (button.getX() - 1) && j == button.getY()) || (i == (button.getX() + 1) && j == button.getY()) || (j == (button.getY() - 1) && i == button.getX()) || (j == (button.getY() + 1) && i == button.getX())) {
                        if (engine.getFiled().getCell(i, j).getBoss_id() != engine.getFiled().getCurrentPlayer().getPlayer_id() && !engine.getFiled().getCell(i, j).getClass().getSimpleName().equals("Brick")) {
                            bufArray[i][j].setDisable(false);
                            bufArray[i][j].setOpacity(1);
                        } else {
                            bufArray[i][j].setDisable(true);
                            bufArray[i][j].setOpacity(0.35);
                        }

                    } else {
                        bufArray[i][j].setDisable(true);
                        bufArray[i][j].setOpacity(0.35);
                    }
                }
            }
            button.setDisable(false);
            button.setOpacity(1);
        } else {
            boolean result;
            if (!button.equals(warringCell)) {
                button.getStyleClass().clear();
                bufArray[button.getX()][button.getY()].getStyleClass().add("player" + engine.getFiled().getCell(button.getX(), button.getY()).getBoss_id() + "-" + engine.getFiled().getCell(button.getX(), button.getY()).getDices() + "-DOWN");
                Player bufPlayer = null;
                for (Player player : engine.getFiled().getPlayers()) {
                    if (player.getPlayer_id() == engine.getFiled().getCell(button.getX(), button.getY()).getBoss_id()) {
                        bufPlayer = player;
                        break;
                    }
                }
                labelText += "   VS   " + bufPlayer.getName() + ": " + engine.getFiled().getCell(button.getX(), button.getY()).getDices() + " dices";
                result = engine.battle(warringCell.getX(), warringCell.getY(), button.getX(), button.getY(), engine.getFiled());
                label.setText(labelText);
                if (result) {
                    labelText = engine.getFiled().getCurrentPlayer().getName() + " WIN";
                } else {
                    labelText = bufPlayer.getName() + " WIN";
                }
            } else {
                button.getStyleClass().clear();
                bufArray[button.getX()][button.getY()].getStyleClass().add("player" + engine.getFiled().getCell(button.getX(), button.getY()).getBoss_id() + "-" + engine.getFiled().getCell(button.getX(), button.getY()).getDices());
                labelText = "";
            }
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                    e -> {
                        reloadFiled();
                        warringCell = null;
                        label.setText(labelText);
                    }
            ));
            timeline.play();

        }
    }


    private void reloadFiled() {
        for (int i = 0; i < engine.getFiled().getFiled().length; i++) {
            for (int j = 0; j < engine.getFiled().getFiled()[0].length; j++) {
                if (engine.getFiled().getCell(i, j).getClass().getSimpleName().equals("Brick")) {
                    bufArray[i][j].getStyleClass().clear();
                    bufArray[i][j].getStyleClass().add("BRICK");
                } else {
                    bufArray[i][j].getStyleClass().clear();
                    bufArray[i][j].getStyleClass().add("player" + engine.getFiled().getCell(i, j).getBoss_id() + "-" + engine.getFiled().getCell(i, j).getDices());
                }
                if (engine.getFiled().getCell(i, j).getBoss_id() != engine.getFiled().getCurrentPlayer().getPlayer_id()) {
                    bufArray[i][j].setDisable(true);
                    bufArray[i][j].setOpacity(0.35);
                } else {
                    bufArray[i][j].setDisable(false);
                    bufArray[i][j].setOpacity(1);
                }
            }
        }
    }

    private void fillingGUIFiled(Engine engine, ObservableList<Node> root) {
        for (Node n : root) {
            if (n instanceof GridPane) {
                GridPane gp = (GridPane) n;
                gp.getChildren().clear();
                int cols = gp.getColumnConstraints().size();
                int rows = gp.getRowConstraints().size();
                bufArray = new UserButton[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        UserButton tmp = new UserButton(i, j);
                        bufArray[i][j] = tmp;
                        if (engine.getFiled().getCell(i, j).getClass().getSimpleName().equals("Brick")) {
                            bufArray[i][j].getStyleClass().add("BRICK");
                            bufArray[i][j].setOpacity(0.35);
                        } else {
                            bufArray[i][j].getStyleClass().add("player" + engine.getFiled().getCell(i, j).getBoss_id() + "-" + engine.getFiled().getCell(i, j).getDices());
                            bufArray[i][j].setOnAction(e -> {
                                handleButtonAction(tmp);
                            });
                            if (engine.getFiled().getCell(i, j).getBoss_id() != engine.getFiled().getCurrentPlayer().getPlayer_id()) {
                                tmp.setDisable(true);
                                tmp.setOpacity(0.35);
                            }
                        }
                        bufArray[i][j].setPrefSize(70, 110);
                        bufArray[i][j].setMaxSize(70, 110);
                        bufArray[i][j].setMinSize(70, 110);

                        gp.add(tmp, j, i);
                    }
                }
            }
        }

    }

    void playerToList(String name, ArrayList<Player> players) {
        players.trimToSize();
        players.add(new Player(name, (byte) (players.size() + 1)));
        players.trimToSize();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("DiceWars v1.0");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        primaryStage.setResizable(false);


        FXMLLoader loaderMenu = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent paneMenu = loaderMenu.load();
        Scene menuScene = new Scene(paneMenu, 947.0, 614.0);
        menuScene.getStylesheets().add(GUI.class.getResource("textures/styleButton.css").toExternalForm());
        primaryStage.setScene(menuScene);
        primaryStage.show();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        final Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root, 957.0, 614.0);
        scene.getStylesheets().add(GUI.class.getResource("textures/styleButton.css").toExternalForm());
        Button endTurn = (Button) root.lookup("#endTurnBtn");
        endTurn.setOnAction(eventEndTurn -> {
            if (filed.fillingBonusDices()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CONGRATULATION");
                alert.setHeaderText(null);
                alert.setContentText(filed.getCurrentPlayer().getName() + " WIN!!!");
                alert.showAndWait();
                primaryStage.setScene(menuScene);
                primaryStage.show();
            }
            filed.setCurrentPlayer();
            reloadFiled();
        });
        Button BackInMenu = (Button) root.lookup("#inMenuBtn");
        BackInMenu.setOnAction(eventInMenu -> {
            primaryStage.setScene(menuScene);
            primaryStage.show();
        });
        Button SaveGame = (Button) root.lookup("#saveBtn");
        SaveGame.setOnAction(eventSave -> {
            SaveGame sv = new SaveGame();
            sv.toByteArray(filed);
        });
        label = (Label) root.lookup("#Label");

        Button newGame = (Button) paneMenu.lookup("#newGameBtn");
        newGame.setOnAction(eventNewGame -> {
            ArrayList<Player> players = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                TextInputDialog dialog = new TextInputDialog("name");
                dialog.setTitle("Input name");
                String number = null;
                if (i == 1) {
                    number = "first";
                }
                if (i == 2) {
                    number = "second";
                }
                dialog.setHeaderText("Enter the name of the " + number + " player");
                dialog.setContentText("Enter your name:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> playerToList(name, players));
            }
            players.trimToSize();
            if (players.size() == 2) {
                filed = new Filed(5, 12, players);
                engine = Engine.getEngine(filed);
                fillingGUIFiled(engine, root.getChildrenUnmodifiable());
                primaryStage.setScene(scene);
                primaryStage.show();

            } else {
                primaryStage.setScene(menuScene);
                primaryStage.show();
            }
        });
        Button load = (Button) paneMenu.lookup("#loadGameBtn");
        load.setOnAction(eventLoad -> {
            SaveGame sv = new SaveGame();
            filed = sv.getSave();
            if (filed != null) {
                engine = Engine.getEngine(filed);
                fillingGUIFiled(engine, root.getChildrenUnmodifiable());
                label.setText("");
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                primaryStage.setScene(menuScene);
                primaryStage.show();
            }
        });
        Button exit = (Button) paneMenu.lookup("#ExitBtn");
        exit.setOnAction(eventExit -> {
            System.exit(0);
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
