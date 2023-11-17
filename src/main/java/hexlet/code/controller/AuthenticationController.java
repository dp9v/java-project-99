package hexlet.code.controller;

import hexlet.code.dto.AuthRequest;
import hexlet.code.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthenticationController.PATH)
public class AuthenticationController {
    public static final String PATH = "/api/login";

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public String create(@RequestBody AuthRequest authRequest) {
        var authToken = new UsernamePasswordAuthenticationToken(
            authRequest.username(), authRequest.password()
        );
        authenticationManager.authenticate(authToken);
        return jwtUtils.generateToken(authRequest.username());
    }
}
