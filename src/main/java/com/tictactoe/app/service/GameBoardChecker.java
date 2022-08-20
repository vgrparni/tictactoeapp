package com.tictactoe.app.service;

import static com.tictactoe.app.utility.ConstantsUtility.EIGHT;
import static com.tictactoe.app.utility.ConstantsUtility.FIVE;
import static com.tictactoe.app.utility.ConstantsUtility.FOUR;
import static com.tictactoe.app.utility.ConstantsUtility.GAME_DRAW;
import static com.tictactoe.app.utility.ConstantsUtility.NINE;
import static com.tictactoe.app.utility.ConstantsUtility.ONE;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_1;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_2;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_O;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_O_WIN_LINE;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_X;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_X_WIN_LINE;
import static com.tictactoe.app.utility.ConstantsUtility.SEVEN;
import static com.tictactoe.app.utility.ConstantsUtility.SIX;
import static com.tictactoe.app.utility.ConstantsUtility.THREE;
import static com.tictactoe.app.utility.ConstantsUtility.TWO;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tictactoe.app.openapi.model.Player;

@Component
public class GameBoardChecker {
	private static final Logger log = LoggerFactory.getLogger(GameBoardChecker.class);
	
	public String checkWinningPossibility(Map<String, String> gameBoard) {
		int possibleWinLineCount = 8;
		for (int line = 0; line < possibleWinLineCount; line++) {
			String winLine = null;
			switch (line) {
			case 0:
				winLine = gameBoard.get(ONE) + gameBoard.get(TWO) + gameBoard.get(THREE);
				break;
			case 1:
				winLine = gameBoard.get(FOUR) + gameBoard.get(FIVE) + gameBoard.get(SIX);
				break;
			case 2:
				winLine = gameBoard.get(SEVEN) + gameBoard.get(EIGHT) + gameBoard.get(NINE);
				break;
			case 3:
				winLine = gameBoard.get(ONE) + gameBoard.get(FOUR) + gameBoard.get(SEVEN);
				break;
			case 4:
				winLine = gameBoard.get(TWO) + gameBoard.get(FIVE) + gameBoard.get(EIGHT);
				break;
			case 5:
				winLine = gameBoard.get(THREE) + gameBoard.get(SIX) + gameBoard.get(NINE);
				break;
			case 6:
				winLine = gameBoard.get(ONE) + gameBoard.get(FIVE) + gameBoard.get(NINE);
				break;
			case 7:
				winLine = gameBoard.get(THREE) + gameBoard.get(FIVE) + gameBoard.get(SEVEN);
				break;
			default:
				break;
			}
			if (PLAYER_X_WIN_LINE.equals(winLine)) {
				return PLAYER_X;
			} else if (PLAYER_O_WIN_LINE.equals(winLine)) {
				return PLAYER_O;
			}

		}
		if (isGameDraw(gameBoard)) {
			return GAME_DRAW;
		}
		return null;
	}

	public boolean isGameDraw(Map<String, String> gameBoard) {
		long movesCount = gameBoard.values().stream().filter(Objects::nonNull).count();
		return movesCount == 8;
	}

	public Boolean validatePlayersTurn(String player,Map<String, String> gameBoard) {
		log.info("--:Validating Players move:--");
		Map<String, Long> playerWiseMovesCountMap = gameBoard.values().stream().filter(Objects::nonNull)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		if (playerWiseMovesCountMap.size() == 2) {
			long firstPlayerOccupencyCount = playerWiseMovesCountMap.get(PLAYER_X);
			long secondPlayerOccupencyCount = playerWiseMovesCountMap.get(PLAYER_O);
			long difference = firstPlayerOccupencyCount > secondPlayerOccupencyCount
					? firstPlayerOccupencyCount - secondPlayerOccupencyCount
					: secondPlayerOccupencyCount - firstPlayerOccupencyCount;
			if ((difference > 1 && (firstPlayerOccupencyCount > secondPlayerOccupencyCount && PLAYER_X.equals(player))
					|| (secondPlayerOccupencyCount > firstPlayerOccupencyCount && PLAYER_O.equals(player)))) {
				log.info("--:Player-{} has taken wrong turn:--", player);
				return Boolean.FALSE;
			}
		} else if (playerWiseMovesCountMap.size() == 1) {
			if (playerWiseMovesCountMap.containsKey(player)) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;

	}
	
	public Player findWinner(Map<String, String> gameBoard) {
		String winner = checkWinningPossibility(gameBoard);
		Player playerWinner;
		if (winner != null) {
			switch (winner) {
			case PLAYER_X:
				playerWinner = new Player();
				playerWinner.setId(winner);
				playerWinner.setDescription(PLAYER_1);
				break;
			case PLAYER_O:
				playerWinner = new Player();
				playerWinner.setId(winner);
				playerWinner.setDescription(PLAYER_2);
				break;
			case GAME_DRAW:
				playerWinner = new Player();
				playerWinner.setId(GAME_DRAW);
				playerWinner.setDescription("Noone wins, Its a tie!");
				break;
			default:
				playerWinner = null;
			}
			return playerWinner;
		}
		return null;

	}

	
}
