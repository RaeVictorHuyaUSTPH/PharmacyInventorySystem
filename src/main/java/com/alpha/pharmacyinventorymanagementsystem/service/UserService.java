package com.alpha.pharmacyinventorymanagementsystem.service;

import com.alpha.pharmacyinventorymanagementsystem.constant.Role;
import com.alpha.pharmacyinventorymanagementsystem.dto.CreateUserDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.UserDTO;
import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import com.alpha.pharmacyinventorymanagementsystem.entity.User;
import com.alpha.pharmacyinventorymanagementsystem.exception.ElementNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private static final String USERNOTFOUNDERROR = "User Not Found in the Database.";

    public UserDTO addUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setUserId(user.getUserId());
        BeanUtils.copyProperties(createUserDto, user);
        userRepository.save(user);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        log.info("Adding User:{}:{}: to the database.", userDTO.getUserId(),
                userDTO.getUserName());
        return userDTO;
    }
    public List<UserDTO> filterUserByRole(Role userRole) throws ElementNotFoundException {
        List<User> sortUser = userRepository.findByUserRole(userRole);
        List<UserDTO> sortUserDtos = new ArrayList<>();
        if (sortUser.isEmpty()) {
            log.error(USERNOTFOUNDERROR);
            throw new ElementNotFoundException(USERNOTFOUNDERROR);
        }
        for (User user : sortUser) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            sortUserDtos.add(userDTO);
        }
        return sortUserDtos;
    }
    public List<UserDTO> findAllUser() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTOS.add(userDTO);
        }
        log.info("Retrieving all the user in the database.");
        return userDTOS;
    }

    public UserDTO findUser(Integer id) throws ElementNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.error(USERNOTFOUNDERROR);
            throw new ElementNotFoundException(USERNOTFOUNDERROR);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        log.info("Retrieving User:{}:{}: data.", userDTO.getUserId(), userDTO.getUserName());
        return userDTO;
    }

    public UserDTO updateUser(int id, CreateUserDto createUserDto) throws ElementNotFoundException {
        User editUser = userRepository.findById(id).orElse(null);
        if (editUser == null) {
            log.error(USERNOTFOUNDERROR);
            throw new ElementNotFoundException(USERNOTFOUNDERROR);
        }
        BeanUtils.copyProperties(createUserDto, editUser);
        userRepository.save(editUser);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(editUser, userDTO);
        log.info("{} was updated successfully.", userDTO.getUserName());
        return userDTO;
    }

    public void deleteUser(int id) throws ElementNotFoundException {
        User deleteUser = userRepository.findById(id).orElse(null);
        if (deleteUser == null) {
            log.error(USERNOTFOUNDERROR);
            throw new ElementNotFoundException(USERNOTFOUNDERROR);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(deleteUser, userDTO);
        log.info("Deleting User id: {}: {}: to the database.", userDTO.getUserId(),
                userDTO.getUserName());
        userRepository.deleteById(id);
    }
}


