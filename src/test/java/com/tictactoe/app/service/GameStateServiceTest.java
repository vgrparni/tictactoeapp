package com.tictactoe.app.service;

import static org.junit.Assert.assertEquals;
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
import com.tictactoe.app.utility.ConstantsUtility;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameStateServiceTest {
	private static final String NEW_GAME_INFO_PATH = "/tictactoe/startNewGame";
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
		TurnRequest turnRequest = new TurnRequest();
		turnRequest.setPlayerId("X");
		turnRequest.setPosition(1);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(turnRequest);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/tictactoe/playerTurn")
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
		TurnRequest turnRequest = new TurnRequest();
		turnRequest.setPosition(2);
		turnRequest.setPlayerId("O");
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(turnRequest);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/tictactoe/playerTurn")
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().is(400));
	}

	@Test
	public void checkHorizontalWinningToPlayerX() throws Exception {

		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "X");
		existingGameBoard.put("2", "X");
		existingGameBoard.put("4", "O");
		existingGameBoard.put("5", "O");
		gameStateService.getGameBoard(existingGameBoard);
		TurnRequest turnRequest = new TurnRequest();
		turnRequest.setPlayerId("X");
		turnRequest.setPosition(3);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(turnRequest);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/tictactoe/playerTurn")
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();

		TurnResponse expectedResponse = new TurnResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(ConstantsUtility.PLAYER_X);
		expectedwinner.setDescription(ConstantsUtility.PLAYER_1);
		expectedResponse.setGameOver(Boolean.FALSE);
		expectedResponse.setState(existingGameBoard);
		expectedResponse.setWinner(expectedwinner);
		assertEquals(ow.writeValueAsString(expectedResponse), responseActual.getContentAsString());

	}
	
	@Test
	public void checkHorizontalWinningToPlayerO() throws Exception {

		Map<String, String> existingGameBoard = getGameBoardValues();
		existingGameBoard.put("1", "X");
		existingGameBoard.put("7", "X");
		existingGameBoard.put("4", "O");
		existingGameBoard.put("5", "O");
		existingGameBoard.put("8", "X");
		gameStateService.getGameBoard(existingGameBoard);
		TurnRequest turnRequest = new TurnRequest();
		turnRequest.setPlayerId("O");
		turnRequest.setPosition(6);
		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(turnRequest);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/tictactoe/playerTurn")
				.accept(MediaType.APPLICATION_JSON).content(json).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();

		TurnResponse expectedResponse = new TurnResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(ConstantsUtility.PLAYER_O);
		expectedwinner.setDescription(ConstantsUtility.PLAYER_2);
		expectedResponse.setGameOver(Boolean.FALSE);
		expectedResponse.setState(existingGameBoard);
		expectedResponse.setWinner(expectedwinner);
		assertEquals(ow.writeValueAsString(expectedResponse), responseActual.getContentAsString());

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

}
