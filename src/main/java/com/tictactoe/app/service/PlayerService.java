package com.tictactoe.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoePlayersApiDelegate;
import com.tictactoe.app.openapi.model.Player;

@Service
public class PlayerService implements TictactoePlayersApiDelegate {

	@Override
	public ResponseEntity<List<Player>> getPlayersInfo() {
		Player player1 = new Player();
		Player player2 = new Player();
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(player1);
		playerList.add(player2);
		return new ResponseEntity<>(playerList, HttpStatus.OK);
	}
}
