package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.CourierClient;
import ru.yandex.practicum.scooter.api.model.Courier;
import ru.yandex.practicum.scooter.api.model.CourierCredentials;
import ru.yandex.practicum.scooter.api.model.ErrorResponse;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.practicum.scooter.api.model.Courier.getRandomCourier;

@DisplayName("Authorization")
public class LoginCourierTest {
    Courier courier;
    CourierClient courierClient = new CourierClient();

    @Before
    public void setUp(){
        courier = getRandomCourier();
        courierClient.createCourier(courier);
    }

    @After
    public void tearDown() {
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLogin = courierClient.login(courierCredentials);
        if (responseLogin.statusCode() == SC_OK) {
            int courierId = responseLogin.body().jsonPath().getInt("id");
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Authorization with correct login and correct password")
    public void loginSuccessTest() {
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());

        Response responseLogin = courierClient.login(courierCredentials);

        assertEquals(SC_OK, responseLogin.statusCode());
        int courierId = responseLogin.body().jsonPath().getInt("id");
        assertTrue(courierId > 0);
    }

    @Test
    @DisplayName("Authorization with incorrect login and password")
    public void incorrectLoginTest() {
        CourierCredentials courierCredentials = new CourierCredentials("incorrect_login", courier.getPassword());

        Response responseLogin = courierClient.login(courierCredentials);

        assertEquals(SC_NOT_FOUND, responseLogin.statusCode());
        ErrorResponse errorResponse = responseLogin.as(ErrorResponse.class);
        assertEquals("Учетная запись не найдена", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Authorization with correct login and incorrect password")
    public void incorrectPasswordTest() {
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), "incorrect_password");

        Response responseLogin = courierClient.login(courierCredentials);

        assertEquals(SC_NOT_FOUND, responseLogin.statusCode());
        ErrorResponse errorResponse = responseLogin.as(ErrorResponse.class);
        assertEquals("Учетная запись не найдена", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Authorization with incorrect login and incorrect password")
    public void incorrectCredentialsTest() {
        CourierCredentials courierCredentials = new CourierCredentials("incorrect_login", "incorrect_password");

        Response responseLogin = courierClient.login(courierCredentials);

        assertEquals(SC_NOT_FOUND, responseLogin.statusCode());
        ErrorResponse errorResponse = responseLogin.as(ErrorResponse.class);
        assertEquals("Учетная запись не найдена", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Authorization with empty login and correct password")
    public void emptyLoginTest() {
        CourierCredentials courierCredentials = new CourierCredentials("", courier.getPassword());

        Response responseLogin = courierClient.login(courierCredentials);

        assertEquals(SC_BAD_REQUEST, responseLogin.statusCode());
        ErrorResponse errorResponse = responseLogin.as(ErrorResponse.class);
        assertEquals("Недостаточно данных для входа", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Authorization with correct login and empty password")
    public void emptyPasswordTest() {
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), "");

        Response responseLogin = courierClient.login(courierCredentials);

        assertEquals(SC_BAD_REQUEST, responseLogin.statusCode());
        ErrorResponse errorResponse = responseLogin.as(ErrorResponse.class);
        assertEquals("Недостаточно данных для входа", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Authorization with empty login and empty password")
    public void emptyCredentialsTest() {
        CourierCredentials courierCredentials = new CourierCredentials("", "");

        Response responseLogin = courierClient.login(courierCredentials);

        assertEquals(SC_BAD_REQUEST, responseLogin.statusCode());
        ErrorResponse errorResponse = responseLogin.as(ErrorResponse.class);
        assertEquals("Недостаточно данных для входа", errorResponse.getMessage());
    }
}
