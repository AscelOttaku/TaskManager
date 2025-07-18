package kg.com.taskmanager.service.impl;

import kg.com.taskmanager.config.jwt.JwtService;
import kg.com.taskmanager.dto.auth.AuthenticationResponse;
import kg.com.taskmanager.dto.auth.SignInRequest;
import kg.com.taskmanager.dto.auth.SignUpRequest;
import kg.com.taskmanager.dto.mapper.SignRequestMapper;
import kg.com.taskmanager.enums.RoleEnum;
import kg.com.taskmanager.exceptions.AlreadyExistsException;
import kg.com.taskmanager.exceptions.InvalidPasswordException;
import kg.com.taskmanager.model.User;
import kg.com.taskmanager.repository.RoleRepository;
import kg.com.taskmanager.repository.UserRepository;
import kg.com.taskmanager.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final SignRequestMapper signRequestMapper;

    @Override
    public AuthenticationResponse authenticate(SignUpRequest request) {
        log.info("Идет регистрация пользователя с почтой: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Пользователь с почтой " + request.getEmail() + " уже существует");
        }

        User users = signRequestMapper.mapToModel(request);
        users.setRole(roleRepository.findByRoleName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new NoSuchElementException("Роль USER не найдена")));

        userRepository.save(users);

        String jwtToken = jwtService.generateToken(users.getEmail());
        log.info("Пользователь с почтой {} успешно зарегистрирован", request.getEmail());

        return new AuthenticationResponse(jwtToken, users.getEmail(), users.getRole().getRoleName());
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        log.info("Идет вход пользователя с почтой: {}", signInRequest.getEmail());

        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            log.warn("Пароль пользователя с почтой {} не совпадает", signInRequest.getEmail());
            throw new InvalidPasswordException("Invalid password");
        }

        String jwtToken = jwtService.generateToken(user.getEmail());
        log.info("Пользователь с почтой {} успешно вошел в систему", signInRequest.getEmail());
        return new AuthenticationResponse(jwtToken, user.getEmail(), user.getRole().getRoleName());
    }
}