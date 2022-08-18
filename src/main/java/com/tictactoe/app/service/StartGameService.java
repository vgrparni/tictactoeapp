package com.tictactoe.app.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoeApiDelegate;
import com.tictactoe.app.openapi.model.NewGameInfo;

@Service
public class StartGameService implements TictactoeApiDelegate {
	private String MESSAGE = "Hello Mr.X and Mr.O your game started!,All the best and enjoy playing";
	
   @Override
   public ResponseEntity<NewGameInfo> startNewGame() {
	   NewGameInfo newGameInfo = new NewGameInfo();
	   newGameInfo.setMessage(MESSAGE);
	   Map<String, String> gameBoard  = new HashMap<>();
       newGameInfo.board(gameBoard);
	 return new ResponseEntity<NewGameInfo>(newGameInfo, HttpStatus.CREATED);
   }
}
