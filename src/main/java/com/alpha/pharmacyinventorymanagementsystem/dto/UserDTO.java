package com.alpha.pharmacyinventorymanagementsystem.dto;

import com.alpha.pharmacyinventorymanagementsystem.constant.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO implements Serializable {
    @Schema(example = "1", description = "This is the user id.")
    private int userId;
    @Schema(example = "name", description = "This is the user name.")
    private String userName;
    @Schema(example = "CUSTOMER", description = "This is the user role.")
    private Role userRole;
}
