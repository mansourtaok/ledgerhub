package com.ledgerhub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerhub.model.db.User;
import com.ledgerhub.service.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final IUserService userService;

	public UserController(IUserService userService) {
		this.userService = userService;
	}

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        //return userService.create(user);
    	return null ;
    }
    
    // READ ALL
    @GetMapping
    public List<User> findAll() {
        //return userService.findAll();
        return null ;
    }
	
    
    // READ BY ID
    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        //return userService.findById(id);
        return null ;
    }
    
    
    // UPDATE
    @PutMapping("/{id}")
    public User update(
            @PathVariable Long id,
            @RequestBody User user) {
        //return userService.update(id, user);
        return null ;
    }
    
    // SOFT DELETE
    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        //userService.deactivate(id);
    }

    // HARD DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        //userService.delete(id);
    }
}
