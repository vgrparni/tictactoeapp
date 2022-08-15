package com.tictactoe.app.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

}
