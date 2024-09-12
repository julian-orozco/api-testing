package com.testing.api.utils;

import com.github.javafaker.Faker;
import com.testing.api.models.Client;

public class ClientUtil {

    private static final Faker faker = new Faker();

    public static Client createRandomClient() {
        return Client.builder()
                .name(faker.name().firstName())
                .lastName(faker.name().lastName())
                .country(faker.address().country())
                .city(faker.address().city())
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .id(faker.idNumber().valid())
                .build();
    }
}