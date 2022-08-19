package com.tictactoe.app.service;

import static com.tictactoe.app.utility.ConstantsUtility.EIGHT;
import static com.tictactoe.app.utility.ConstantsUtility.FIVE;
import static com.tictactoe.app.utility.ConstantsUtility.FOUR;
import static com.tictactoe.app.utility.ConstantsUtility.GAME_DRAW;
import static com.tictactoe.app.utility.ConstantsUtility.MESSAGE;
import static com.tictactoe.app.utility.ConstantsUtility.NINE;
import static com.tictactoe.app.utility.ConstantsUtility.ONE;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_1;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_2;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_O;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_X;
import static com.tictactoe.app.utility.ConstantsUtility.SEVEN;
import static com.tictactoe.app.utility.ConstantsUtility.SIX;
import static com.tictactoe.app.utility.ConstantsUtility.THREE;
import static com.tictactoe.app.utility.ConstantsUtility.TWO;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoeApiDelegate;
import com.tictactoe.app.openapi.model.NewGameInfo;
import com.tictactoe.app.openapi.model.Player;
import com.tictactoe.app.openapi.model.TurnRequest;
import com.tictactoe.app.openapi.model.TurnResponse;

@Service
public class GameStateService implements TictactoeApiDelegate {
	private static final Logger log = LoggerFactory.getLogger(GameStateService.class);
	private Map<String, String> gameBoard = new HashMap<>();
	private Boolean IsGameEnd = Boolean.FALSE;
	@Autowired
	private GameBoardChecker gameBoardChecker;

	@Override
	public ResponseEntity<NewGameInfo> startNewGame() {
		NewGameInfo newGameInfo = new NewGameInfo();
		newGameInfo.setMessage(MESSAGE);
		gameBoard.put(ONE, null);
		gameBoard.put(TWO, null);
		gameBoard.put(THREE, null);
		gameBoard.put(FOUR, null);
		gameBoard.put(FIVE, null);
		gameBoard.put(SIX, null);
		gameBoard.put(SEVEN, null);
		gameBoard.put(EIGHT, null);
		gameBoard.put(NINE, null);
		newGameInfo.gameboard(gameBoard);
		this.IsGameEnd = Boolean.FALSE;
		log.info("New game started!.");
		return new ResponseEntity<NewGameInfo>(newGameInfo, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<TurnResponse> playerTurn(TurnRequest turnRequest) {
		TurnResponse turnResponse = new TurnResponse();
		if (IsGameEnd) {
			log.info("--:Hi Player-{}, Game over already:--", turnRequest.getPlayerId());
			turnResponse.setGameOver(Boolean.TRUE);
			turnResponse.setState(gameBoard);
			turnResponse.setWinner(findWinner(gameBoard));
			return new ResponseEntity<TurnResponse>(turnResponse, HttpStatus.OK);
		} else {
			if (gameBoardChecker.validatePlayersTurn(turnRequest.getPlayerId(), gameBoard)) {
				if (gameBoard.get(String.valueOf(turnRequest.getPosition())) != null) {
					log.info("--:Player-{} trying wrong move, Same position not allowed:--", turnRequest.getPlayerId());
					return new ResponseEntity<TurnResponse>(HttpStatus.BAD_REQUEST);
				}
				gameBoard.put(turnRequest.getPosition().toString(), turnRequest.getPlayerId());
				turnResponse.setGameOver(IsGameEnd);
				turnResponse.setState(gameBoard);
				turnResponse.setWinner(findWinner(gameBoard));
				return new ResponseEntity<TurnResponse>(turnResponse, HttpStatus.OK);
			} else {
				log.info("--:Player-{} trying wrong move,Twice not allowed:--", turnRequest.getPlayerId());
				return new ResponseEntity<TurnResponse>(HttpStatus.BAD_REQUEST);
			}
		}
	}

	public void getGameBoard(Map<String, String> gameBoard) {
		this.gameBoard = gameBoard;
	}

	private Player findWinner(Map<String, String> gameBoard) {
		String winner = gameBoardChecker.checkWinningPossibility(gameBoard);
		Player playerWinner;
		if (winner != null) {
			switch (winner) {
			case PLAYER_X:
				playerWinner = new Player();
				playerWinner.setId(winner);
				playerWinner.setDescription(PLAYER_1);
				gameEnd(Boolean.TRUE);
				break;
			case PLAYER_O:
				playerWinner = new Player();
				playerWinner.setId(winner);
				playerWinner.setDescription(PLAYER_2);
				gameEnd(Boolean.TRUE);
				break;
			case GAME_DRAW:
				playerWinner = new Player();
				playerWinner.setId(GAME_DRAW);
				playerWinner.setDescription("Noone wins, Its a tie!");
				gameEnd(Boolean.TRUE);
				break;
			default:
				playerWinner = null;
			}
			return playerWinner;
		}
		return null;

	}

	public void gameEnd(Boolean isGameOVer) {
		this.IsGameEnd = isGameOVer;
	}

}
