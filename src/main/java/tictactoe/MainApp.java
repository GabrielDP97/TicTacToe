package main.java.tictactoe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;

public class MainApp extends Application {

    private boolean isXTurn = true; // Controla el turno del jugador
    private Button[][] board = new Button[3][3]; // Tablero de 3x3
    private Text turnText = new Text(); // Texto para mostrar el turno
    private boolean gameEnded = false; // Controla si el juego ha terminado
    private Button resetButton; // Botón para reiniciar el juego
    private String playerXName = "Jugador X"; // Nombre del jugador X
    private String playerOName = "Jugador O"; // Nombre del jugador O

    @Override
    public void start(Stage primaryStage) {
        // Pedir los nombres de los jugadores
        askPlayerNames();

        VBox root = new VBox();
        root.setSpacing(20); // Espaciado entre elementos
        root.getStyleClass().add("root-container");

        // Configurar texto inicial para mostrar el turno
        turnText.setText("Turno: " + playerXName);
        turnText.getStyleClass().add("turn-text");

        // Crear el tablero de juego
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("game-board");

        // Configurar filas y columnas del GridPane para que cada celda ocupe el mismo espacio
        for (int i = 0; i < 3; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(33.33); // Divide equitativamente el espacio vertical
            gridPane.getRowConstraints().add(row);

            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33); // Divide equitativamente el espacio horizontal
            gridPane.getColumnConstraints().add(col);
        }

        // Crear las celdas del tablero
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button cell = new Button();
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Forzar el tamaño máximo
                cell.getStyleClass().add("cell-button");

                final int row = i;
                final int col = j;

                cell.setOnAction(e -> handleMove(cell, row, col));

                board[i][j] = cell;
                gridPane.add(cell, j, i); // Añadir celda al tablero
            }
        }

        // Botón para reiniciar el juego
        resetButton = new Button("Reiniciar Juego");
        resetButton.getStyleClass().add("reset-button");
        resetButton.setOnAction(e -> resetGame());

        root.getChildren().addAll(turnText, gridPane, resetButton);

        Scene scene = new Scene(root, 400, 500);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void askPlayerNames() {
        TextInputDialog dialogX = new TextInputDialog("Jugador X");
        dialogX.setTitle("Nombre del Jugador X");
        dialogX.setHeaderText("Introduce el nombre del Jugador X:");
        dialogX.setContentText("Nombre:");

        Optional<String> resultX = dialogX.showAndWait();
        resultX.ifPresent(name -> playerXName = name);

        TextInputDialog dialogO = new TextInputDialog("Jugador O");
        dialogO.setTitle("Nombre del Jugador O");
        dialogO.setHeaderText("Introduce el nombre del Jugador O:");
        dialogO.setContentText("Nombre:");

        Optional<String> resultO = dialogO.showAndWait();
        resultO.ifPresent(name -> playerOName = name);
    }

    private void handleMove(Button cell, int row, int col) {
        if (gameEnded || !cell.getText().isEmpty()) {
            return; // No permite jugar en una celda ya ocupada o si el juego ha terminado
        }

        String player = isXTurn ? "X" : "O";
        String playerName = isXTurn ? playerXName : playerOName;

        cell.setText(player);
        cell.getStyleClass().add(isXTurn ? "x-player" : "o-player");

        // Verificar si el jugador actual ha ganado
        if (checkWinner(player)) {
            gameEnded = true;
            showWinnerAlert(playerName);
            return;
        }

        // Verificar si hay un empate
        if (isBoardFull()) {
            gameEnded = true;
            showDrawAlert();
            return;
        }

        // Cambiar turno
        isXTurn = !isXTurn;
        turnText.setText("Turno: " + (isXTurn ? playerXName : playerOName));
    }

    private boolean checkWinner(String player) {
        // Verificar filas
        for (int i = 0; i < 3; i++) {
            if (board[i][0].getText().equals(player) &&
                board[i][1].getText().equals(player) &&
                board[i][2].getText().equals(player)) {
                return true;
            }
        }

        // Verificar columnas
        for (int j = 0; j < 3; j++) {
            if (board[0][j].getText().equals(player) &&
                board[1][j].getText().equals(player) &&
                board[2][j].getText().equals(player)) {
                return true;
            }
        }

        // Verificar diagonales
        if (board[0][0].getText().equals(player) &&
            board[1][1].getText().equals(player) &&
            board[2][2].getText().equals(player)) {
            return true;
        }

        if (board[0][2].getText().equals(player) &&
            board[1][1].getText().equals(player) &&
            board[2][0].getText().equals(player)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWinnerAlert(String playerName) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Juego terminado");
        alert.setHeaderText(null);
        alert.setContentText("¡El jugador " + playerName + " ha ganado!");
        alert.showAndWait();
    }

    private void showDrawAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Juego terminado");
        alert.setHeaderText(null);
        alert.setContentText("¡Es un empate!");
        alert.showAndWait();
    }

    private void resetGame() {
        gameEnded = false;
        isXTurn = true;
        turnText.setText("Turno: " + playerXName);

        // Limpiar el tablero
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setText("");
                board[i][j].getStyleClass().removeAll("x-player", "o-player");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
