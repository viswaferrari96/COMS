package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.GET, path = "/users")
    public List<User> getAllUsers() {
        List<User> results = userRepository.findAll();
        return results;
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/users/{userId}")
    public Optional<User> findUserById(@PathVariable("userId") int id) {
        Optional<User> results = userRepository.findById(id);
        return results;
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/{name}/{wins}")
    public List<User> saveUser(@PathVariable("userId") int id, @PathVariable("name") String name, @PathVariable("wins") int wins) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setWins(wins);
        userRepository.save(user);
        return userRepository.findAll();
    }
}
