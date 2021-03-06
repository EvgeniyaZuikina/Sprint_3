package ru.yandex.practicum.scooter.api.model;

import lombok.Data;

@Data
public class CourierCredentials {
    private String login;
    private String password;

    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
