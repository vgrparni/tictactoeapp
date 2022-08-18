package com.tictactoe.app.service;

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
	private String MESSAGE = "Hello Mr.X and Mr.O your game started!,All the best and enjoy playing";
	
   @Override
   public ResponseEntity<NewGameInfo> startNewGame() {
	   NewGameInfo newGameInfo = new NewGameInfo();
	   newGameInfo.setMessage(MESSAGE);
	   Map<String, String> gameBoard  = new HashMap<>();
	   gameBoard.put("1", null);
	   gameBoard.put("2", null);
	   gameBoard.put("3", null);
	   gameBoard.put("4", null);
	   gameBoard.put("5", null);
	   gameBoard.put("6", null);
	   gameBoard.put("7", null);
	   gameBoard.put("8", null);
	   gameBoard.put("9", null);
       newGameInfo.gameboard(gameBoard);
       log.info("New game started!.");
	 return new ResponseEntity<NewGameInfo>(newGameInfo, HttpStatus.CREATED);
   }
   @Override
public ResponseEntity<TurnResponse> playerTurn(TurnRequest turnRequest) {
	// TODO Auto-generated method stub
	return TictactoeApiDelegate.super.playerTurn(turnRequest);
}
}
