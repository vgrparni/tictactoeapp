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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoeApiDelegate;
import com.tictactoe.app.openapi.model.NewGameInfo;
import com.tictactoe.app.openapi.model.TurnRequest;
import com.tictactoe.app.openapi.model.TurnResponse;

@Service
public class GameStateService implements TictactoeApiDelegate {
	private static final Logger log = LoggerFactory.getLogger(GameStateService.class);
	private Map<String, String> gameBoard = new HashMap<>();

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
		log.info("New game started!.");
		return new ResponseEntity<NewGameInfo>(newGameInfo, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<TurnResponse> playerTurn(TurnRequest turnRequest) {
		if (gameBoard.get(String.valueOf(turnRequest.getPosition())) != null) {
			return new ResponseEntity<TurnResponse>(HttpStatus.BAD_REQUEST);
		}
		gameBoard.put(turnRequest.getPosition().toString(), turnRequest.getPlayerId());
		TurnResponse turnResponse = new TurnResponse();
		turnResponse.setGameOver(Boolean.FALSE);
		turnResponse.setState(gameBoard);
		return new ResponseEntity<TurnResponse>(turnResponse, HttpStatus.OK);
	}

	public void getGameBoard(Map<String, String> gameBoard) {
		this.gameBoard = gameBoard;
	}
}
