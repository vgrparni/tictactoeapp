package com.tictactoe.app.service;

import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_1;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_2;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_O;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_X;

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
		player1.setId(PLAYER_X);
		player1.setDescription(PLAYER_1);
		Player player2 = new Player();
		player2.setId(PLAYER_O);
		player2.setDescription(PLAYER_2);
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(player1);
		playerList.add(player2);
		return new ResponseEntity<List<Player>>(playerList, HttpStatus.OK);
	}
}
