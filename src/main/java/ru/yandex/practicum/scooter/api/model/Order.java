package ru.yandex.practicum.scooter.api.model;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class Order {
    private Integer id;
    private Integer courierId;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private Integer track;
    private String[] color;
    private String comment;
    private Date createdAt;
    private Date updatedAt;
    private Integer status;

    public Order(String firstName, String lastName, String address, String metroStation, String phone, Integer rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color.toArray(new String[0]);
    }

    public static Order getRandomOrder() {
        String firstName = RandomStringUtils.randomAlphabetic(10);
        String lastName = RandomStringUtils.randomAlphabetic(10);
        String address = RandomStringUtils.randomAlphabetic(10);
        String metroStation = RandomStringUtils.randomAlphabetic(10);
        String phone = RandomStringUtils.randomAlphabetic(11);
        Integer rentTime = RandomUtils.nextInt(1, 7);
        String deliveryDate = LocalDate.now().plusDays(1).toString();
        String comment = RandomStringUtils.randomAlphabetic(10);
        List<String> color = Arrays.asList("BLACK", "GRAY");

        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}