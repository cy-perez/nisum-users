package nisum.users.controller.util;

import nisum.users.controller.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class ResponseBuilder {

    public static <T> ResponseEntity<ResponseDTO<T>> build200Response(T data) {
        return ResponseEntity.ok(
                ResponseDTO.<T>builder()
                        .transactionId(UUID.randomUUID().toString())
                        .message("Exitoso")
                        .code(200)
                        .data(data)
                        .build());
    }

    public static <T> ResponseEntity<ResponseDTO<T>> build200DeletedResponse(T data) {
        return ResponseEntity.ok(
                ResponseDTO.<T>builder()
                        .transactionId(UUID.randomUUID().toString())
                        .message("Borrado exitoso")
                        .code(200)
                        .data(data)
                        .build());
    }

    public static <T> ResponseEntity<ResponseDTO<T>> build201Response(T data) {
        return ResponseEntity.ok(
                ResponseDTO.<T>builder()
                        .transactionId(UUID.randomUUID().toString())
                        .message("Creacion exitosa")
                        .code(201)
                        .data(data)
                        .build());
    }

    public static <T> ResponseEntity<ResponseDTO<T>> build401Response() {
        return ResponseEntity.status(401).body(
                ResponseDTO.<T>builder()
                        .transactionId(UUID.randomUUID().toString())
                        .message("Fallo la autenticacion..!")
                        .code(401)
                        .build());
    }

    public static <T> ResponseEntity<ResponseDTO<T>> build404Response() {
        return ResponseEntity.status(404).body(
                ResponseDTO.<T>builder()
                        .transactionId(UUID.randomUUID().toString())
                        .message("Registro no encontrado")
                        .code(404)
                        .build());
    }

    public static <T> ResponseEntity<ResponseDTO<T>> build400PasswordResponse() {
        return ResponseEntity.status(400).body(
                ResponseDTO.<T>builder()
                        .transactionId(UUID.randomUUID().toString())
                        .message("Request incorrecta: password invalido, debe tener al menos una letra mayuscula, al menos un dígito y longitud mayor o igual a 8")
                        .code(400)
                        .build());
    }

    public static <T> ResponseEntity<ResponseDTO<T>> build400EmailResponse() {
        return ResponseEntity.status(400).body(
                ResponseDTO.<T>builder()
                        .transactionId(UUID.randomUUID().toString())
                        .message("Request incorrecta: email invalido, debe estar en formato 'example@example.com'")
                        .code(400)
                        .build());
    }

    public static <T> ResponseEntity<ResponseDTO<T>> build500Response(String exception) {
        return ResponseEntity.status(500).body(
                ResponseDTO.<T>builder()
                        .transactionId(UUID.randomUUID().toString())
                        .message("Error interno en el servidor: " + exception)
                        .code(500)
                        .build());
    }
}
