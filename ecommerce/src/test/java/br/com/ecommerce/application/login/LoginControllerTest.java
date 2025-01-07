package br.com.ecommerce.application.login;

import br.com.ecommerce.application.RequestSender;
import br.com.ecommerce.application.login.dto.LoginRequest;
import br.com.ecommerce.application.util.IntegrationTest;
import br.com.ecommerce.domain.command.CreateUserCommand;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
// Import the users to implement the tests
@Sql(scripts = "/insert-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class LoginControllerTest extends RequestSender {
    @ParameterizedTest
    @MethodSource("provideInvalidLoginParameters")
    void shouldReturnBadRequestWhenParametersAreInvalid(LoginRequest loginRequest, List<String> expectedErrors) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedErrors.toArray())));
    }

    static Stream<Arguments> provideInvalidLoginParameters() {
        return Stream.of(
                Arguments.of(new LoginRequest("log", "log@123"), List.of("login.must.not.be.invalid.email", "password.must.not.violate.pattern")),
                Arguments.of(new LoginRequest(null, null), List.of("login.must.not.be.null", "password.must.not.be.null")),
                Arguments.of(new LoginRequest("jun@jun.com", "Junit@1234"), List.of("login.or.password.invalid"))
        );
    }

    @Test
    void shouldLoginUserAndReturnAccessToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("jun@jun.com", "Junit@123");
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiresIn", Matchers.equalTo(3600)));
    }
}