package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.scooter.api.OrderClient;
import ru.yandex.practicum.scooter.api.model.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.practicum.scooter.api.model.Order.getRandomOrder;

@RunWith(Parameterized.class)
@DisplayName("Order creation")
public class CreateOrderTest {
    Order order;
    OrderClient orderClient = new OrderClient();

    public CreateOrderTest(boolean isBlack, boolean isGray) {
        List<String> colors = new ArrayList<>();

        if (isBlack) { colors.add("BLACK"); }
        if (isGray) { colors.add("GRAY"); }

        order = getRandomOrder();
        order.setColor(colors);
    }

    @Parameterized.Parameters(name = "colors(BLACK={0}, GRAY={1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {false, false},
                {true, false},
                {false, true},
                {true, true},
        });
    }

    @Test
    public void trackNumTest() {
        Response response = orderClient.createOrder(order);

        assertEquals(SC_CREATED, response.statusCode());
        int track = response.body().jsonPath().getInt("track");
        assertTrue(track > 0);
    }
}
