package com.alpha.pharmacyinventorymanagementsystem.controller;

import com.alpha.pharmacyinventorymanagementsystem.dto.CreateUserDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.UserDTO;
import com.alpha.pharmacyinventorymanagementsystem.exception.ElementNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.exception.InvalidUserRoleException;
import com.alpha.pharmacyinventorymanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
@Tag(name = "User Management", description = "API for managing the users.")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Adding user to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success,User added to the database",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request, check your input if it follows the required data type",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateUserDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "Conflict, Check whether your Role is CUSTOMER or PHARMACIST",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database",
                    content = @Content)
    })

    @PostMapping("/addUser")
    public ResponseEntity<UserDTO> addUser(@RequestBody CreateUserDto createUserDto) {
        log.info("Adding new user");
        UserDTO userDTO;
        try {
            userDTO = userService.addUser(createUserDto);
        } catch (InvalidUserRoleException invalidUserRoleException) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Retrieving all the users in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success,Retrieval of all user in the database was successful.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @GetMapping("/users")
    public List<UserDTO> findAllUser() {
        log.info("Fetching all user");
        return userService.findAllUser();
    }

    @Operation(summary = "Retrieving specific user in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, User retrieved successfully.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found, User not found in the database.",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO>
    findUserById(@Parameter(description = "User ID that needs to be retrieved.", example = "1")
                 @PathVariable int id) {
        log.info("Fetching specific user");
        UserDTO userDTO;
        try {
            userDTO = userService.findUser(id);
        } catch (ElementNotFoundException userNotFoundException) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Update existing user in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, User data updated successfully.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request, check your input",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found, User not found in the inventory.",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Conflict, Check whether your Role is CUSTOMER or PHARMACIST",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @PutMapping("/updateUser")
    public ResponseEntity<UserDTO> updateUser(@Parameter(description = "User ID that needs to be updated.")
                                              @RequestParam int id,
                                              @RequestBody CreateUserDto createUserDto) {
        log.info("Update existing user");
        UserDTO userDTO;
        try {
            userDTO = userService.updateUser(id, createUserDto);
        } catch (ElementNotFoundException userNotFoundException) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (InvalidUserRoleException invalidUserRoleException) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Removing the user in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success, Successfully removing this user to the database.",
                    content = {@Content}),
            @ApiResponse(responseCode = "404", description = "Not Found, User not found in the database.",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<StringBuilder>
    deleteUser(@Parameter(description = "User ID that needs to be deleted.") @PathVariable int id) {
        log.info("Deleting User");
        try {
            userService.deleteUser(id);
        } catch (ElementNotFoundException userNotFoundException) {
            return new ResponseEntity<>(new StringBuilder("Failed to delete User ")
                    .append(id).append(" to the database because ").append(id)
                    .append(" is not found in the database."), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new StringBuilder("Successfully deleted User ").append(id).
                append(" to the database."), new HttpHeaders(), HttpStatus.OK);
    }
}
