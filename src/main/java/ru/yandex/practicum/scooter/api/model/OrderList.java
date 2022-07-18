package ru.yandex.practicum.scooter.api.model;

import lombok.Data;
import java.util.List;

@Data
public class OrderList {
    private List<Order> orders;
}
