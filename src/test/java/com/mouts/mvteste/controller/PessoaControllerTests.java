package com.mouts.mvteste.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
public class PessoaControllerTests {

	@Autowired
    private MockMvc mockMvc;

    @Test
    public void testSalvarPessoa() throws Exception {
        
    	String requestBody = "{\"nome\": \"Joao\", \"cpf\": \"00123\", \"dataNascimento\": \"2000-07-01\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/pessoa")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Joao"));
    }

    @Test
    public void testBuscarPorIdExistente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pessoa/{id}", 1))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Joao"));
    }

    @Test
    public void testBuscarPorIdNaoExistente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pessoa/{id}", 999))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

 
    @Test
    public void testAlterarPessoaNaoExistente() throws Exception {
        String requestBody = "{\"idPessoa\": 999, \"nome\": \"Novo Nome\", \"cpf\": \"12345678900\", \"dataNascimento\": \"1990-01-01\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/pessoa")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
