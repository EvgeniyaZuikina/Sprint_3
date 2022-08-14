package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.scooter.api.OrderClient;
import ru.yandex.practicum.scooter.api.model.Order;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.practicum.scooter.api.model.Order.getRandomOrder;

@RunWith(Parameterized.class)
@DisplayName("Order creation")
public class CreateOrderTest {
    Order order;
    OrderClient orderClient = new OrderClient();
    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
        order = getRandomOrder();
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{"GREY"}},
                {new String[]{}}

        };
    }

    @Test
    public void trackNumTest() {
        Response response = orderClient.createOrder(order);

        assertEquals(SC_CREATED, response.statusCode());
        int track = response.body().jsonPath().getInt("track");
        assertTrue(track > 0);
    }
}
