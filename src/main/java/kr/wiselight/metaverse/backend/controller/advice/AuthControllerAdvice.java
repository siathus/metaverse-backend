package kr.wiselight.metaverse.backend.controller.advice;

import io.jsonwebtoken.JwtException;
import kr.wiselight.metaverse.backend.controller.AuthController;
import kr.wiselight.metaverse.backend.controller.UserController;
import kr.wiselight.metaverse.backend.controller.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {AuthController.class, UserController.class})
public class AuthControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponseDto illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return ErrorResponseDto.builder()
                .message(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ErrorResponseDto jwtExceptionHandler(JwtException ex) {
        return ErrorResponseDto.builder()
                .message(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RestClientException.class)
    public ErrorResponseDto httpServerErrorExceptionHandler(RestClientException ex) {
        return ErrorResponseDto.builder()
                .message(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        return ErrorResponseDto.builder()
                .message(ex.getMessage())
                .build();
    }

}
