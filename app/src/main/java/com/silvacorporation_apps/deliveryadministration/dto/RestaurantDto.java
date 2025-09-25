package com.silvacorporation_apps.deliveryadministration.dto;

public class RestaurantDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String imageUrl;
    private Integer capacity;
    private String openTime;
    private String closeTime;
    private Long userId; // 👈 importante para saber quién lo creó

    // Constructor vacío (necesario para Retrofit/Gson)
    public RestaurantDto() {}

    // Constructor mínimo
    public RestaurantDto(String name, String description, String address, String phone) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
    }

    // ✅ Constructor completo (9 parámetros, usado en AddRestaurantBottomSheet)
    public RestaurantDto(String name,
                         String description,
                         String address,
                         String phone,
                         String imageUrl,
                         Integer capacity,
                         String openTime,
                         String closeTime,
                         Long userId) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.capacity = capacity;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.userId = userId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getOpenTime() { return openTime; }
    public void setOpenTime(String openTime) { this.openTime = openTime; }

    public String getCloseTime() { return closeTime; }
    public void setCloseTime(String closeTime) { this.closeTime = closeTime; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "RestaurantDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", capacity=" + capacity +
                ", openTime='" + openTime + '\'' +
                ", closeTime='" + closeTime + '\'' +
                ", userId=" + userId +
                '}';
    }
}
