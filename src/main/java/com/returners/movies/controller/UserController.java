package com.returners.movies.controller;

import com.returners.movies.model.DataResponse;
import com.returners.movies.model.Movie;
import com.returners.movies.model.User;
import com.returners.movies.constants.Constants;
import com.returners.movies.exception.NoSuchUserExistsException;
import com.returners.movies.model.DataResponse;
import com.returners.movies.service.UserService;
import com.returners.movies.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseUtil.getSuccessResponse(users,"All users fetched") ;
    }

    @GetMapping({"/{userId}"})
    public ResponseEntity<DataResponse> getUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        return ResponseUtil.getSuccessResponse(user, "All users fetched");
    }

    @DeleteMapping({"/delete/{userId}"})
    public ResponseEntity<DataResponse> deleteUserById(@PathVariable("userId") Long userId) throws NoSuchUserExistsException {
        if(userService.deleteUserById(userId)) return ResponseUtil.getSuccessResponse(null, String.format(Constants.DELETED_SUCCESSFULLY, userId));
        else throw new NoSuchUserExistsException(String.format(Constants.ID_DOES_NOT_EXISTS, userId));
    }
}
