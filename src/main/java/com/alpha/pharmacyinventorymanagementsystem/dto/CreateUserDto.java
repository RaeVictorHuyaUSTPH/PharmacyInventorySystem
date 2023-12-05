package com.alpha.pharmacyinventorymanagementsystem.dto;

import com.alpha.pharmacyinventorymanagementsystem.constant.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateUserDto implements Serializable {
    @Schema(example = "name", description = "This is the user name.")
    @NotNull
    private String userName;
    @Schema(example = "CUSTOMER", description = "This is the user role.")
    @NotNull
    private Role userRole;
}
