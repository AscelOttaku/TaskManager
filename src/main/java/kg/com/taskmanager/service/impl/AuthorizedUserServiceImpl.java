package kg.com.taskmanager.service.impl;

import kg.com.taskmanager.model.User;
import kg.com.taskmanager.service.AuthorizedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizedUserServiceImpl implements AuthorizedUserService {

    @Override
    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken)
            throw new IllegalArgumentException("user is not authenticated");

        return (User) authentication.getPrincipal();
    }

    @Override
    public Long getAuthorizedUserId() {
        User authUser = getAuthUser();
        return authUser.getId();
    }
}
