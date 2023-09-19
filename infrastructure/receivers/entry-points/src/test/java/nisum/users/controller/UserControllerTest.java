package nisum.users.controller;

import nisum.users.controller.auth.JWTGenerator;
import nisum.users.controller.auth.JWTValidator;
import nisum.users.controller.dto.UserRequestDTO;
import nisum.users.controller.dto.UserResponseDTO;
import nisum.users.controller.util.ResponseBuilder;
import nisum.users.domain.common.model.User;
import nisum.users.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class UserControllerTest {

    private final UserService userService = Mockito.mock(UserService.class);
    private final JWTGenerator jWTGenerator = Mockito.mock(JWTGenerator.class);
    private final JWTValidator jWTValidator = Mockito.mock(JWTValidator.class);
    private final UserController controller = new UserController(userService, jWTGenerator, jWTValidator);


    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(controller, "passwordRegex", "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
        ReflectionTestUtils.setField(controller, "emailRegex", "^[A-Za-z0-9+_.-]+@(.+)$");
    }

    @Test
    void saveUserTestWhenResponse201() {
        var expect = ResponseBuilder.build201Response(
                UserResponseDTO.builder()
                        .email("example@example.com")
                        .build());

        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("Hunter123")
                .phones(List.of())
                .build();

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);
        Mockito.when(userService.findById(anyString())).thenReturn(null);
        Mockito.when(userService.save(any(User.class))).thenReturn(User.builder().email("example@example.com").phones(List.of()).build());

        var result = controller.saveUser(request, "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getData().getEmail(), expect.getBody().getData().getEmail());
    }

    @Test
    void saveUserTestWhenResponseEmailExist() {
        var expect = "Request incorrecta: el correo del usuario que desea registrar ya existe";

        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("Hunter123")
                .build();

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);
        Mockito.when(userService.findById(anyString())).thenReturn(User.builder().build());

        var result = controller.saveUser(request, "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }

    @Test
    void saveUserTestWhenResponseBadRequestEmail() {
        var expect = "Request incorrecta: email invalido, debe estar en formato 'example@example.com'";

        var request = UserRequestDTO.builder()
                .email("example.com")
                .build();

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);
        Mockito.when(userService.findById(anyString())).thenReturn(null);

        var result = controller.saveUser(request, "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }

    @Test
    void saveUserTestWhenResponseBadRequestPassword() {
        var expect = "Request incorrecta: password invalido, debe tener al menos una letra mayuscula, al menos un dígito y longitud mayor o igual a 8";

        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("test")
                .build();

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);
        Mockito.when(userService.findById(anyString())).thenReturn(null);

        var result = controller.saveUser(request, "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }

    @Test
    void saveUserTestUnauthorized() {
        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("Hunter123")
                .build();

        var expect = "Fallo la autenticacion..!";

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(false);

        var result = controller.saveUser(request, "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(401));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }

    @Test
    void findByEmailTest() {
        var expect = ResponseBuilder.build200Response(
                UserResponseDTO.builder()
                        .email("example@example.com")
                        .build());

        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .phones(List.of())
                .build();

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);
        Mockito.when(userService.findById(anyString())).thenReturn(User.builder().email("example@example.com").phones(List.of()).build());

        var result = controller.findByEmail(request.getEmail(), "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getData().getEmail(), expect.getBody().getData().getEmail());

    }

    @Test
    void findByEmailTestUnauthorized() {
        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("Hunter123")
                .build();

        var expect = "Fallo la autenticacion..!";

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(false);

        var result = controller.findByEmail(request.getEmail(), "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(401));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }

    @Test
    void findAllUsersTest() {

        User user = User.builder().email("example@example.com").phones(List.of()).build();
        var expect = ResponseBuilder.build200Response(
                UserResponseDTO.builder()
                        .email("example@example.com")
                        .build());

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);
        Mockito.when(userService.findAll()).thenReturn(List.of(user));

        var result = controller.findAllUsers("test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
    }

    @Test
    void findAllUsersTestUnauthorized() {
        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("Hunter123")
                .build();

        var expect = "Fallo la autenticacion..!";

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(false);

        var result = controller.findAllUsers("test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(401));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }

    @Test
    void updateUserTest() {
        var expect = ResponseBuilder.build200Response(
                UserResponseDTO.builder()
                        .email("example@example.com")
                        .build());

        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("Hunter123")
                .phones(List.of())
                .build();

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);
        Mockito.when(userService.findById(anyString())).thenReturn(User.builder().email("example@example.com").phones(List.of()).build());
        Mockito.when(userService.update(any(User.class))).thenReturn(User.builder().email("example@example.com").phones(List.of()).build());

        var result = controller.updateUser(request, "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getData().getEmail(), expect.getBody().getData().getEmail());
    }

    @Test
    void updateUserTestUnauthorized() {
        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("Hunter123")
                .build();

        var expect = "Fallo la autenticacion..!";

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(false);

        var result = controller.updateUser(request, "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(401));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }

    @Test
    void updateUserTestWhenResponseBadRequestEmail() {
        var expect = "Request incorrecta: email invalido, debe estar en formato 'example@example.com'";

        var request = UserRequestDTO.builder()
                .email("example.com")
                .build();

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);

        var result = controller.updateUser(request, "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }

    @Test
    void updateUserTestWhenResponseBadRequestPassword() {
        var expect = "Request incorrecta: password invalido, debe tener al menos una letra mayuscula, al menos un dígito y longitud mayor o igual a 8";

        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("test")
                .build();

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);

        var result = controller.updateUser(request, "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }

    @Test
    void deleteByEmailTest() {
        var expect = ResponseBuilder.build200Response(
                UserResponseDTO.builder()
                        .email("example@example.com")
                        .build());

        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .phones(List.of())
                .build();

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(true);
        Mockito.when(userService.deleteById(anyString())).thenReturn(User.builder().email("example@example.com").phones(List.of()).build());

        var result = controller.deleteByEmail(request.getEmail(), "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getData().getEmail(), expect.getBody().getData().getEmail());

    }

    @Test
    void deleteByEmailTestUnauthorized() {
        var request = UserRequestDTO.builder()
                .email("example@example.com")
                .password("Hunter123")
                .build();

        var expect = "Fallo la autenticacion..!";

        Mockito.when(jWTValidator.isValidToken(anyString())).thenReturn(false);

        var result = controller.deleteByEmail(request.getEmail(), "test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(401));
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), expect);
    }
}