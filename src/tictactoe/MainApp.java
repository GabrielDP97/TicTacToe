package tictactoe;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    private Label statusLabel; // Etiqueta para mensajes
    private Button[][] boardButtons = new Button[3][3]; // Botones del tablero
    private String currentPlayer = "X"; // Jugador actual

    @Override
    public void start(Stage primaryStage) {
        // Crear el tablero de 3x3
        GridPane board = new GridPane();
        board.setAlignment(Pos.CENTER);
        board.setHgap(10);
        board.setVgap(10);

        // Crear botones y añadirlos al tablero
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button();
                button.setPrefSize(100, 100);
                boardButtons[row][col] = button;

                // Configurar acción al hacer clic
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(e -> handleMove(finalRow, finalCol));

                board.add(button, col, row);
            }
        }

        // Crear la etiqueta para mensajes
        statusLabel = new Label("¡Turno del jugador X!");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Botón para reiniciar el juego
        Button restartButton = new Button("Reiniciar");
        restartButton.setOnAction(e -> resetGame());

        // Diseñar la ventana principal
        VBox root = new VBox(20, board, statusLabel, restartButton);
        root.setAlignment(Pos.CENTER);

        // Crear y mostrar la escena
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Manejar movimientos de los jugadores
    private void handleMove(int row, int col) {
        Button button = boardButtons[row][col];
        if (button.getText().isEmpty()) {
            button.setText(currentPlayer); // Marcar el movimiento
            button.setDisable(true); // Deshabilitar el botón

            if (checkWinner()) {
                statusLabel.setText("¡El jugador " + currentPlayer + " ha ganado!");
                disableBoard();
            } else if (isBoardFull()) {
                statusLabel.setText("¡Es un empate!");
            } else {
                currentPlayer = currentPlayer.equals("X") ? "O" : "X"; // Cambiar turno
                statusLabel.setText("¡Turno del jugador " + currentPlayer + "!");
            }
        }
    }

    // Verificar si hay un ganador
    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            // Filas
            if (!boardButtons[i][0].getText().isEmpty() &&
                boardButtons[i][0].getText().equals(boardButtons[i][1].getText()) &&
                boardButtons[i][1].getText().equals(boardButtons[i][2].getText())) {
                return true;
            }
            // Columnas
            if (!boardButtons[0][i].getText().isEmpty() &&
                boardButtons[0][i].getText().equals(boardButtons[1][i].getText()) &&
                boardButtons[1][i].getText().equals(boardButtons[2][i].getText())) {
                return true;
            }
        }
        // Diagonales
        if (!boardButtons[0][0].getText().isEmpty() &&
            boardButtons[0][0].getText().equals(boardButtons[1][1].getText()) &&
            boardButtons[1][1].getText().equals(boardButtons[2][2].getText())) {
            return true;
        }
        if (!boardButtons[0][2].getText().isEmpty() &&
            boardButtons[0][2].getText().equals(boardButtons[1][1].getText()) &&
            boardButtons[1][1].getText().equals(boardButtons[2][0].getText())) {
            return true;
        }
        return false;
    }

    // Verificar si el tablero está lleno
    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (boardButtons[row][col].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Deshabilitar el tablero (después de ganar o empatar)
    private void disableBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                boardButtons[row][col].setDisable(true);
            }
        }
    }

    // Reiniciar el juego
    private void resetGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                boardButtons[row][col].setText("");
                boardButtons[row][col].setDisable(false);
            }
        }
        currentPlayer = "X";
        statusLabel.setText("¡Turno del jugador X!");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
