/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.fhnw.medicalinformatics;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class GeneControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void simpleTest() throws Exception {
		ResultActions ra = this.mockMvc.perform(get("/geneservice/test")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.geneId").value(1234));
	}

	@Test
	public void retrieveGeneByIdTest() throws Exception {
		ResultActions ra = this.mockMvc.perform(get("/geneservice/byid").param("id", "11494039")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.geneId").value(11494039));
	}
}
