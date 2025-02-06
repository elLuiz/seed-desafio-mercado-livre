package br.com.ecommerce.application;

import br.com.ecommerce.application.util.IntegrationTest;
import br.com.ecommerce.domain.command.CreateAccountCommand;
import br.com.ecommerce.service.user.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
class CreateAccountControllerTest extends RequestSender {
    @Autowired
    UserRepository userRepository;

    @ParameterizedTest
    @MethodSource("provideInvalidOutputs")
    void shouldReturnBadRequestWhenInputIsInvalid(CreateAccountCommand createAccountCommand, List<String> expectedErrors) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                .content(objectMapper.writeValueAsString(createAccountCommand))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedErrors.toArray())));
    }

    static Stream<Arguments> provideInvalidOutputs() {
        return Stream.of(
                Arguments.of(new CreateAccountCommand(null, null, null), List.of("full.name.must.not.be.null", "login.must.not.be.null", "password.must.not.be.null")),
                Arguments.of(new CreateAccountCommand("name".repeat(31), "test@", "Password@102202902902902"), List.of("full.name.must.not.surpass.120.characters", "login.must.not.be.invalid.email", "password.must.not.violate.pattern")),
                Arguments.of(new CreateAccountCommand("Invalid name 092901", "test@test.com.br", "Password@10"), List.of("name.must.not.violate.pattern"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidUsers")
    void shouldCreateUsers(CreateAccountCommand createAccountCommand) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                .content(objectMapper.writeValueAsString(createAccountCommand))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", Matchers.equalTo(createAccountCommand.fullName())));
    }

    static Stream<Arguments> provideValidUsers() {
        return Stream.of(
                Arguments.of(new CreateAccountCommand("Max Verstappen", "vert@ver.com", "WorldChampion@24")),
                Arguments.of(new CreateAccountCommand("Ronaldo Fenômeno", "gol@gmail.com", "StrongKnees@199>")),
                Arguments.of(new CreateAccountCommand("AVS".repeat(40), "email@email.com", "@@tronApas112211"))
        );
    }

    @Test
    void shouldNotCreateUserWithDuplicateEmail() throws Exception {
        CreateAccountCommand createAccountCommand = new CreateAccountCommand("Max Verstappen", "vert1@ver.com", "WorldChampion@24");
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                .content(objectMapper.writeValueAsString(createAccountCommand))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", Matchers.equalTo(createAccountCommand.fullName())));

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                .content(objectMapper.writeValueAsString(createAccountCommand))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.contains("login.must.be.unique")));
    }

}