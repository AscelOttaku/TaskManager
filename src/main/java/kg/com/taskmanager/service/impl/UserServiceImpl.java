package kg.com.taskmanager.service.impl;

import kg.com.taskmanager.repository.UserRepository;
import kg.com.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public boolean isUserEmailIsUnique(String email) {
        if (email == null || email.isBlank())
            return false;

        return !userRepository.existsByEmail(email);
    }

}
