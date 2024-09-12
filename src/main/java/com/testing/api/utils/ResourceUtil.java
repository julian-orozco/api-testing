package com.testing.api.utils;

import com.github.javafaker.Faker;
import com.testing.api.models.Client;
import com.testing.api.models.Resource;

public class ResourceUtil {

    private static final Faker faker = new Faker();

    public static Resource createRandomResource() {
        return Resource.builder()
                .name(faker.commerce().productName())
                .trademark(faker.company().name())
                .stock(faker.number().numberBetween(1, 100000))
                .price((float) faker.number().randomDouble(2, 10, 1000))
                .description(faker.lorem().sentence())
                .tags(faker.lorem().words(3).toString())
                .active(true)
                .id(faker.idNumber().valid())
                .build();
    }
}