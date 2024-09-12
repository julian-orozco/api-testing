package com.testing.api.models;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private String name;
    private String trademark;
    private int stock;
    private float price;
    private String description;
    private String tags;
    private boolean active;
    private String id;

}
