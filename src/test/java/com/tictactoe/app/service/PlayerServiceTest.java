package com.tictactoe.app.service;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.app.openapi.model.Player;
import static com.tictactoe.app.utility.ConstantPathUtility.PLAYERS_INFO_ENDPOINT;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlayerServiceTest {

	private final int BOARD_GAME_TEAM_SIZE = 2;
	private final String EXPECTED_TEAM_INFO = "[{\"id\":\"X\",\"description\":\"Player1\"},{\"id\":\"O\",\"description\":\"Player2\"}]";
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void checkGetPlayerEndPointAvailable() throws Exception {
		this.mockMvc.perform(get(PLAYERS_INFO_ENDPOINT)).andExpect(status().is(200));
	}

	@Test
	public void checkPlayersCount() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(PLAYERS_INFO_ENDPOINT);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseBody = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Player> playerList = objectMapper.readValue(responseBody, List.class);
		assertEquals(BOARD_GAME_TEAM_SIZE, playerList.size());
	}

	@Test
	public void checkPlayersInformation() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(PLAYERS_INFO_ENDPOINT);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseBody = result.getResponse().getContentAsString();
		assertEquals(EXPECTED_TEAM_INFO, responseBody);
	}

}
