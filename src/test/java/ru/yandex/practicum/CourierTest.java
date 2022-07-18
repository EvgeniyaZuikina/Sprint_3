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
import ru.yandex.practicum.scooter.api.model.CreateCourierResponse;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.practicum.scooter.api.model.Courier.getRandomCourier;

@DisplayName("Courier creation")
public class CourierTest {
    Courier courier;
    CourierClient courierClient = new CourierClient();

    @Before
    public void init() {
        courier = getRandomCourier();
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
    @DisplayName("Successful courier creation")
    public void courierTest() {
        Response responseCreate = courierClient.createCourier(courier);

        assertEquals(SC_CREATED, responseCreate.statusCode());
        CreateCourierResponse createCourierResponse = responseCreate.as(CreateCourierResponse.class);
        assertTrue(createCourierResponse.ok);
    }

    @Test
    @DisplayName("Creation two identical couriers")
    public void createDuplicateCourierTest() {
        Response responseCreate = courierClient.createCourier(courier);

        assertEquals(SC_CREATED, responseCreate.statusCode());
        CreateCourierResponse createCourierResponse = responseCreate.as(CreateCourierResponse.class);
        assertTrue(createCourierResponse.getOk());

        responseCreate = courierClient.createCourier(courier);

        assertEquals(SC_CONFLICT, responseCreate.statusCode());
        ErrorResponse errorResponse = responseCreate.as(ErrorResponse.class);
        assertEquals("Этот логин уже используется", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Create courier with empty login")
    public void createCourierWithEmptyLoginTest() {
        courier.setLogin("");

        Response responseCreate = courierClient.createCourier(courier);

        assertEquals(SC_BAD_REQUEST, responseCreate.statusCode());
        ErrorResponse errorResponse = responseCreate.as(ErrorResponse.class);
        assertEquals("Недостаточно данных для создания учетной записи", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Create courier with empty password")
    public void createCourierWithEmptyPasswordTest() {
        courier.setPassword("");

        Response responseCreate = courierClient.createCourier(courier);

        assertEquals(SC_BAD_REQUEST, responseCreate.statusCode());
        ErrorResponse errorResponse = responseCreate.as(ErrorResponse.class);
        assertEquals("Недостаточно данных для создания учетной записи", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Create courier with empty firstname")
    public void createCourierWithEmptyFirstNameTest() {
        courier.setFirstName("");

        Response responseCreate = courierClient.createCourier(courier);

        assertEquals(SC_BAD_REQUEST, responseCreate.statusCode());
        ErrorResponse errorResponse = responseCreate.as(ErrorResponse.class);
        assertEquals("Недостаточно данных для создания учетной записи", errorResponse.getMessage());
    }
}