package com.silvacorporation_apps.deliveryadministration.dto;

import java.io.Serializable;

public class MenuItemDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Double price;
    private boolean available;
    private String imageUrl;
    private Long restaurantId;

    // Constructor vacío
    public MenuItemDto() {}

    // Constructor con parámetros
    public MenuItemDto(Long id, String name, String description, String category,
                       Double price, boolean available, String imageUrl, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.available = available;
        this.imageUrl = imageUrl;
        this.restaurantId = restaurantId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
}
