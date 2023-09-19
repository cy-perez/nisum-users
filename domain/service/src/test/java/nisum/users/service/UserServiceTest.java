package nisum.users.service;

import nisum.users.domain.common.UserGatewayRepository;
import nisum.users.domain.common.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class UserServiceTest {

    private final UserGatewayRepository userGatewayRepository = Mockito.mock(UserGatewayRepository.class);
    private final UserService service = new UserService(userGatewayRepository);

    @Test
    void saveTest() {
        var expect = "TEST";

        Mockito.when(userGatewayRepository.save(any(User.class))).thenReturn(User.builder().id(expect).build());
        var result = service.save(User.builder().build());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expect, result.getId());
    }

    @Test
    void getByEmailTest() {
        var expect = "example@example.com";

        Mockito.when(userGatewayRepository.findById(anyString())).thenReturn(User.builder().email(expect).build());

        var result = service.findById("TEST");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expect, result.getEmail());
    }

    @Test
    void getAllTest() {
        var expect = 2;
        var expectedId1 = "1";
        var expectedId2 = "2";

        Mockito.when(userGatewayRepository.findAll()).thenReturn(List.of(User.builder().id("1").build(), User.builder().id("2").build()));

        var result = service.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expect, result.size());
        Assertions.assertEquals(expectedId1, result.get(0).getId());
        Assertions.assertEquals(expectedId2, result.get(1).getId());
    }

    @Test
    void updateUserTestWhenUserExist() {
        var expect = "example@example.com";

        Mockito.when(userGatewayRepository.findById(anyString())).thenReturn(User.builder().email(expect).build());
        Mockito.when(userGatewayRepository.updateUser(any(User.class), any(User.class))).thenReturn(User.builder().email(expect).build());

        var result = service.update(User.builder().email(expect).build());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expect, result.getEmail());
    }

    @Test
    void updateUserTestWhenUserNoExist() {

        Mockito.when(userGatewayRepository.findById(anyString())).thenReturn(null);

        var result = service.update(User.builder().email("example@example.com").build());

        Assertions.assertNull(result);
    }

    @Test
    void deleteByEmailTest(){
        var expect = "example@example.com";

        Mockito.when(userGatewayRepository.findById(anyString())).thenReturn(User.builder().email(expect).build());
        Mockito.doNothing().when(userGatewayRepository).deleteById(anyString());

        var result = service.deleteById(anyString());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expect, result.getEmail());
    }

    @Test
    void deleteUserTestWhenUserNoExist() {

        Mockito.when(userGatewayRepository.findById(anyString())).thenReturn(null);

        var result = service.deleteById("test");

        Assertions.assertNull(result);
    }
}