package com.example.roommate.application.validation;

import jakarta.validation.constraints.NotBlank;

public record UserForm(
        String title,
        @NotBlank(message = "Can not be Empty") String firstName,
        @NotBlank(message = "Can not be Empty") String surName) {

}