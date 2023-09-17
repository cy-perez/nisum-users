package nisum.users.domain.common;

import nisum.users.domain.common.model.User;

import java.util.List;

public interface UserGatewayRepository {

    User save(User user);

    User findById(String id);

    List<User> findAll();

    void deleteById(String id);

    User updateUser(User userUpdate, User userConsult);

}
