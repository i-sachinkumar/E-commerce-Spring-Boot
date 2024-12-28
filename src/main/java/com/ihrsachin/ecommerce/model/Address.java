package com.ihrsachin.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters long")
    private String street;

    @NotBlank
    private String buildingName;

    @NotBlank
    @Size(min = 3, message = "city name must be at least 3 characters long")
    private String city;

    @NotBlank
    @Size(min = 3, message = "state name must be at least 3 characters long")
    private String state;

    @NotBlank
    @Size(min = 6, message = "zip code must be at least 6 characters long")
    private String zipCode;

    @NotBlank
    @Size(min = 3, message = "country name must be at least 3 characters long")
    private String country;

    @ManyToMany(mappedBy = "addresses")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}
