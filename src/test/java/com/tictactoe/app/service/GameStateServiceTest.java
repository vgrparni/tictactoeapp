package com.tictactoe.app.service;

import static com.tictactoe.app.utility.ConstantPathUtility.NEW_GAME_INFO_PATH;
import static com.tictactoe.app.utility.ConstantPathUtility.PLAYER_TURN_INFO_PATH;
import static com.tictactoe.app.utility.ConstantPathUtility.STATE_OF_GAME_BOARD_PATH;
import static com.tictactoe.app.utility.ConstantsUtility.GAME_DRAW;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_1;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_2;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_O;
import static com.tictactoe.app.utility.ConstantsUtility.PLAYER_X;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tictactoe.app.openapi.model.NewGameInfo;
import com.tictactoe.app.openapi.model.Player;
import com.tictactoe.app.openapi.model.TurnRequest;
import com.tictactoe.app.openapi.model.TurnResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameStateServiceTest {
	private final int GAME_BOARD_POSITIONS_COUNT = 9;
	private final String MESSAGE = "Hello Mr.X and Mr.O your game started!,All the best and enjoy playing";
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private GameStateService gameStateService;

	@Test
	public void checkNewGmeStart() throws Exception {
		this.mockMvc.perform(post(NEW_GAME_INFO_PATH)).andExpect(status().is(201));
	}

	@Test
	public void checkGameBoardPositionsCountAndMessage() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(NEW_GAME_INFO_PATH);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseBody = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		NewGameInfo gameBoardInfo = objectMapper.readValue(responseBody, NewGameInfo.class);

		assertEquals(GAME_BOARD_POSITIONS_COUNT, gameBoardInfo.getGameboard().size());
		assertEquals(MESSAGE, gameBoardInfo.getMessage());
	}

	@Test
	public void checkNewGameBoardEmptyAlways() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(NEW_GAME_INFO_PATH);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseBody = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		NewGameInfo gameBoardInfo = objectMapper.readValue(responseBody, NewGameInfo.class);
		boolean allValuesAreNull = gameBoardInfo.getGameboard().values().stream().allMatch(Objects::isNull);
		assertEquals(Boolean.TRUE, allValuesAreNull);
	}

	@Test
	public void updatePlayer() throws Exception {
		Map<String, String> expectedGameBoard = getGameBoardValues();
		gameStateService.getGameBoard(expectedGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_X, 1));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		TurnResponse expectedResponse = new TurnResponse();
		expectedResponse.setGameOver(Boolean.FALSE);
		expectedResponse.setState(expectedGameBoard);
		assertEquals(ow.writeValueAsString(expectedResponse), responseActual.getContentAsString());
	}

	@Test
	public void ShouldNotUpdateSamePosition() throws Exception {
		Map<String, String> exisitngGameBoard = getGameBoardValues();
		exisitngGameBoard.put("2", "O");
		gameStateService.getGameBoard(exisitngGameBoard);
		gameStateService.gameEnd(Boolean.FALSE);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_O, 2));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().is(400));
	}

	@Test
	public void checkHorizontalWinningToPlayerX() throws Exception {
		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "X");
		existingGameBoard.put("4", "O");
		existingGameBoard.put("2", "X");
		existingGameBoard.put("5", "O");
		gameStateService.getGameBoard(existingGameBoard);
		gameStateService.gameEnd(Boolean.FALSE);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_X, 3));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(PLAYER_X);
		expectedwinner.setDescription(PLAYER_1);
		assertEquals(
				ow.writeValueAsString(prepareExpectedTurnResponse(Boolean.FALSE, existingGameBoard, expectedwinner)),
				responseActual.getContentAsString());
	}

	@Test
	public void checkHorizontalWinningToPlayerO() throws Exception {
		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "X");
		existingGameBoard.put("4", "O");
		existingGameBoard.put("7", "X");
		existingGameBoard.put("5", "O");
		existingGameBoard.put("8", "X");
		gameStateService.getGameBoard(existingGameBoard);
		gameStateService.gameEnd(Boolean.FALSE);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_O, 6));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(PLAYER_O);
		expectedwinner.setDescription(PLAYER_2);
		assertEquals(
				ow.writeValueAsString(prepareExpectedTurnResponse(Boolean.FALSE, existingGameBoard, expectedwinner)),
				responseActual.getContentAsString());
	}

	@Test
	public void checkVerticalWinningToPlayer() throws Exception {
		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "X");
		existingGameBoard.put("2", "O");
		existingGameBoard.put("4", "X");
		existingGameBoard.put("5", "O");
		gameStateService.getGameBoard(existingGameBoard);
		gameStateService.gameEnd(Boolean.FALSE);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_X, 7));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(PLAYER_X);
		expectedwinner.setDescription(PLAYER_1);
		assertEquals(
				ow.writeValueAsString(prepareExpectedTurnResponse(Boolean.FALSE, existingGameBoard, expectedwinner)),
				responseActual.getContentAsString());
	}

	@Test
	public void checkDiagonalWinningPlayer() throws Exception {
		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "X");
		existingGameBoard.put("3", "O");
		existingGameBoard.put("4", "X");
		existingGameBoard.put("5", "O");
		existingGameBoard.put("2", "X");
		gameStateService.getGameBoard(existingGameBoard);
		gameStateService.gameEnd(Boolean.FALSE);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_O, 7));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(PLAYER_O);
		expectedwinner.setDescription(PLAYER_2);
		assertEquals(
				ow.writeValueAsString(prepareExpectedTurnResponse(Boolean.FALSE, existingGameBoard, expectedwinner)),
				responseActual.getContentAsString());
	}

	@Test
	public void checkGameDraw() throws Exception {
		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "X");
		existingGameBoard.put("2", "O");
		existingGameBoard.put("3", "X");
		existingGameBoard.put("4", "O");
		existingGameBoard.put("6", "X");
		existingGameBoard.put("5", "O");
		existingGameBoard.put("7", "X");
		gameStateService.getGameBoard(existingGameBoard);
		gameStateService.gameEnd(Boolean.FALSE);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_O, 9));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(GAME_DRAW);
		expectedwinner.setDescription("Noone wins, Its a tie!");
		assertEquals(
				ow.writeValueAsString(prepareExpectedTurnResponse(Boolean.FALSE, existingGameBoard, expectedwinner)),
				responseActual.getContentAsString());
	}

	@Test
	public void checkGameOver() throws Exception {
		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "X");
		existingGameBoard.put("5", "O");
		existingGameBoard.put("2", "X");
		existingGameBoard.put("4", "O");
		existingGameBoard.put("3", "X");
		gameStateService.getGameBoard(existingGameBoard);
		gameStateService.gameEnd(Boolean.TRUE);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_O, 9));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(PLAYER_X);
		expectedwinner.setDescription(PLAYER_1);
		assertEquals(
				ow.writeValueAsString(prepareExpectedTurnResponse(Boolean.TRUE, existingGameBoard, expectedwinner)),
				responseActual.getContentAsString());
	}

	@Test
	public void validateOnePlayerShouldNotGiveMultipleMoves() throws Exception {
		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "X");
		gameStateService.getGameBoard(existingGameBoard);
		gameStateService.gameEnd(Boolean.FALSE);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_X, 2));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().is(400));
	}

	@Test
	public void validateBothPlayerShouldNotGiveMultipleMoves() throws Exception {
		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "O");
		existingGameBoard.put("2", "X");
		existingGameBoard.put("3", "O");
		existingGameBoard.put("5", "X");
		gameStateService.getGameBoard(existingGameBoard);
		gameStateService.gameEnd(Boolean.FALSE);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(prepareTurnRequest(PLAYER_X, 2));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_INFO_PATH)
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().is(400));
	}

	@Test
	public void checkStateOfGameBoard() throws Exception {
		this.mockMvc.perform(get(STATE_OF_GAME_BOARD_PATH)).andExpect(status().is(200));
	}

	public Map<String, String> getGameBoardValues() {
		Map<String, String> expectedGameBoard = new HashMap<>();
		expectedGameBoard.put("1", null);
		expectedGameBoard.put("2", null);
		expectedGameBoard.put("3", null);
		expectedGameBoard.put("4", null);
		expectedGameBoard.put("5", null);
		expectedGameBoard.put("6", null);
		expectedGameBoard.put("7", null);
		expectedGameBoard.put("8", null);
		expectedGameBoard.put("9", null);
		return expectedGameBoard;
	}

	public TurnRequest prepareTurnRequest(String player, int position) {
		TurnRequest turnRequest = new TurnRequest();
		turnRequest.setPlayerId(player);
		turnRequest.setPosition(position);
		return turnRequest;
	}

	public TurnResponse prepareExpectedTurnResponse(Boolean isGameOver, Map<String, String> gameBoard, Player winner) {
		TurnResponse expectedResponse = new TurnResponse();
		expectedResponse.setGameOver(isGameOver);
		expectedResponse.setState(gameBoard);
		expectedResponse.setWinner(winner);
		return expectedResponse;
	}

}
