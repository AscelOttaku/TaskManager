package kg.com.taskmanager.service;

import kg.com.taskmanager.model.User;

public interface AuthorizedUserService {
    User getAuthUser();

    Long getAuthorizedUserId();
}
