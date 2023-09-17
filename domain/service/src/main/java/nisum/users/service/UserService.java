package nisum.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import nisum.users.domain.common.model.User;
import nisum.users.domain.common.UserGatewayRepository;

import java.util.List;

@Log
@RequiredArgsConstructor
public class UserService {

    private final UserGatewayRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findById(String id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User update(User user) {

        var userConsult = findById(user.getEmail());
        if(userConsult == null) return null;

        return userRepository.updateUser(user, userConsult);
    }

    public User deleteById(String id) {

        var user = findById(id);
        if(user == null) return null;

        userRepository.deleteById(id);
        return user;
    }
}
