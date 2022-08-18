package com.tictactoe.app.service;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.tictactoe.app.openapi.model.NewGameInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StartNewGameServiceTest {
	private static final String NEW_GAME_INFO_PATH = "/tictactoe/startNewGame";
	 @Autowired
	    private MockMvc mockMvc;
	    @Test
	    public void checkNewGmeStart() throws Exception {
	        this.mockMvc.perform(post(NEW_GAME_INFO_PATH)).andExpect(status().is(201));
	    }
        
	    @Test
	    public void checkGameBoardPositionsCountAndMessage() throws Exception {
	    	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(NEW_GAME_INFO_PATH);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			String responseBody=  result.getResponse().getContentAsString();
			ObjectMapper objectMapper = new ObjectMapper();
			NewGameInfo gameBoardInfo = objectMapper.readValue(responseBody, NewGameInfo.class);
			
	        assertEquals(9, gameBoardInfo.getBoard().size());
	        assertEquals("Hello Mr.X and Mr.O your game started!,All the best and enjoy playing", gameBoardInfo.getMessage());
	    }
}
