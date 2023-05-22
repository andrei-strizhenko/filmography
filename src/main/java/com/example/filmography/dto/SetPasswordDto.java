package com.example.filmography.dto;

import lombok.Data;

@Data
public class SetPasswordDto {

    private Long userId;
    private String oldPassword;
    private String newPassword;
}
