package nisum.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import nisum.users.controller.auth.JWTGenerator;
import nisum.users.controller.auth.JWTValidator;
import nisum.users.controller.dto.ResponseDTO;
import nisum.users.controller.dto.UserRequestDTO;
import nisum.users.controller.dto.UserResponseDTO;
import nisum.users.controller.mapper.UserMapper;
import nisum.users.controller.util.ResponseBuilder;
import nisum.users.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@Log
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/nisum-users")
public class UserController {

    private final UserService userService;
    private final JWTGenerator jwtGenerator;
    private final JWTValidator jwtValidator;

    @Value("${regex.password}")
    private String passwordRegex;

    @Value("${regex.email}")
    private String emailRegex;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> saveUser(
            @RequestBody UserRequestDTO userDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (!jwtValidator.isValidToken(authorizationHeader))
                return ResponseBuilder.build401Response();

            if(userService.findById(userDTO.getEmail()) != null)
                return ResponseBuilder.build400EmailExistResponse();

            if (!isValidEmail(userDTO.getEmail()))
                return ResponseBuilder.build400EmailResponse();

            if (!isValidPassword(userDTO.getPassword()))
                return ResponseBuilder.build400PasswordResponse();

            var userSaved = userService.save(UserMapper.mapDtoToUser(userDTO));
            return ResponseBuilder.build201Response(UserMapper.mapUserToDTO(userSaved));
        } catch (Exception ex) {
            return ResponseBuilder.build500Response(ex.getMessage());
        }
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> findByEmail(
            @PathVariable("email") String email,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            if (!jwtValidator.isValidToken(authorizationHeader))
                return ResponseBuilder.build401Response();

            var userConsult = userService.findById(email);
            if (userConsult == null) return ResponseBuilder.build404Response();
            return ResponseBuilder.build200Response(UserMapper.mapUserToDTO(userConsult));
        } catch (Exception ex) {
            return ResponseBuilder.build500Response(ex.getMessage());
        }
    }

    @GetMapping("/view-users")
    public ResponseEntity<ResponseDTO<List<UserResponseDTO>>> findAllUsers(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (!jwtValidator.isValidToken(authorizationHeader))
                return ResponseBuilder.build401Response();

            var usersConsult = userService.findAll();
            var usersDtoConsult = usersConsult.stream().map(UserMapper::mapUserToDTO).toList();
            return ResponseBuilder.build200Response(usersDtoConsult);
        } catch (Exception ex) {
            return ResponseBuilder.build500Response(ex.getMessage());
        }
    }

    @PutMapping("/user/edit")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updateUser(
            @RequestBody UserRequestDTO userDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (!jwtValidator.isValidToken(authorizationHeader))
                return ResponseBuilder.build401Response();

            if (!isValidEmail(userDTO.getEmail()))
                return ResponseBuilder.build400EmailResponse();

            if (!isValidPassword(userDTO.getPassword()))
                return ResponseBuilder.build400PasswordResponse();

            var userUpdated = userService.update(UserMapper.mapDtoToUser(userDTO));
            if (userUpdated == null) return ResponseBuilder.build404Response();

            return ResponseBuilder.build200Response(UserMapper.mapUserToDTO(userUpdated));
        } catch (Exception ex) {
            return ResponseBuilder.build500Response(ex.getMessage());
        }
    }

    @DeleteMapping("/user/delete/{email}")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> deleteByEmail(
            @PathVariable("email") String email,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            if (!jwtValidator.isValidToken(authorizationHeader))
                return ResponseBuilder.build401Response();

            var userConsult = userService.deleteById(email);
            if (userConsult == null) return ResponseBuilder.build404Response();
            return ResponseBuilder.build200DeletedResponse(UserMapper.mapUserToDTO(userConsult));
        } catch (Exception ex) {
            return ResponseBuilder.build500Response(ex.getMessage());
        }
    }

    @GetMapping("/user/auth")
    public ResponseEntity<ResponseDTO<String>> getJwtToken(
            @RequestHeader("Secret") String secret) {
        try {
            var jwtGenerated = jwtGenerator.generate(secret);
            if (jwtGenerated == null) return ResponseBuilder.build401Response();
            return ResponseBuilder.build200Response(jwtGenerated);
        } catch (Exception ex) {
            return ResponseBuilder.build500Response(ex.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        var emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        var passwordPattern = Pattern.compile(passwordRegex);
        return passwordPattern.matcher(password).matches();
    }
}