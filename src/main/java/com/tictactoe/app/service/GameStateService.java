package com.tictactoe.app.service;

import static com.tictactoe.app.utility.ConstantsUtility.EIGHT;
import static com.tictactoe.app.utility.ConstantsUtility.FIVE;
import static com.tictactoe.app.utility.ConstantsUtility.FOUR;
import static com.tictactoe.app.utility.ConstantsUtility.MESSAGE;
import static com.tictactoe.app.utility.ConstantsUtility.NINE;
import static com.tictactoe.app.utility.ConstantsUtility.ONE;
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
	private Boolean isGameEnd = Boolean.FALSE;
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
		newGameInfo.gameboard(this.gameBoard);
		this.isGameEnd = Boolean.FALSE;
		log.info("New game started!.");
		return new ResponseEntity<NewGameInfo>(newGameInfo, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<TurnResponse> playerTurn(TurnRequest turnRequest) {
		if (this.isGameEnd) {
			log.info("--:Hi Player-{}, Game over already:--", turnRequest.getPlayerId());
			return new ResponseEntity<TurnResponse>(preparePlayerTurnResponse(), HttpStatus.OK);
		} else {
			if (gameBoardChecker.validatePlayersTurn(turnRequest.getPlayerId(), this.gameBoard)) {
				if (this.gameBoard.get(String.valueOf(turnRequest.getPosition())) != null) {
					log.info("--:Player-{} trying wrong move, Same position not allowed:--", turnRequest.getPlayerId());
					return new ResponseEntity<TurnResponse>(HttpStatus.BAD_REQUEST);
				}
				this.gameBoard.put(turnRequest.getPosition().toString(), turnRequest.getPlayerId());
				return new ResponseEntity<TurnResponse>(preparePlayerTurnResponse(), HttpStatus.OK);
			} else {
				log.info("--:Player-{} trying wrong move,Twice not allowed:--", turnRequest.getPlayerId());
				return new ResponseEntity<TurnResponse>(HttpStatus.BAD_REQUEST);
			}
		}
	}

	@Override
	public ResponseEntity<Map<String, String>> getStateOfGameBoard() {
		return new ResponseEntity<Map<String, String>>(this.gameBoard, HttpStatus.OK);
	}

	public void getGameBoard(Map<String, String> gameBoard) {
		this.gameBoard = gameBoard;
	}

	public void gameEnd(Boolean isGameOVer) {
		this.isGameEnd = isGameOVer;
	}

	public TurnResponse preparePlayerTurnResponse() {
		TurnResponse turnResponse = new TurnResponse();
		Player winner = gameBoardChecker.findWinner(this.gameBoard);
		if (winner != null) {
			gameEnd(Boolean.TRUE);
		}
		turnResponse.setGameOver(this.isGameEnd);
		turnResponse.setState(this.gameBoard);
		turnResponse.setWinner(winner);
		return turnResponse;
	}
}
