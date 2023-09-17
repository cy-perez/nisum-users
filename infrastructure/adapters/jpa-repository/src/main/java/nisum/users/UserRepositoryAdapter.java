package nisum.users;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nisum.users.domain.common.model.User;
import nisum.users.domain.common.UserGatewayRepository;
import nisum.users.mapper.UserRepositoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserRepositoryAdapter implements UserGatewayRepository {

    private final UserRepository repository;

    @Override
    @Transactional
    public User save(User user) {
        var userSaved = repository.save(UserRepositoryMapper.mapUserToCreateEntity(user));
        return UserRepositoryMapper.mapEntityToUser(userSaved);
    }

    @Override
    public User findById(String id) {
        var userOptional = repository.findById(id);

        return userOptional.map(UserRepositoryMapper::mapEntityToUser).orElse(null);
    }

    @Override
    public List<User> findAll() {
        var usersData = repository.findAll();
        return usersData.stream().map(UserRepositoryMapper::mapEntityToUser).toList();
    }

    @Override
    public void deleteById(String id) {
        var userOptional = repository.findById(id);

        if (userOptional.isPresent()) repository.deleteById(id);
    }

    @Override
    public User updateUser(User userUpdate, User userExist){

        var userSaved = repository.save(UserRepositoryMapper.mapUserToUpdateEntity(userUpdate, userExist));
        return UserRepositoryMapper.mapEntityToUser(userSaved);
    }
}
