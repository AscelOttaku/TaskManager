package kg.com.taskmanager.controller;

import kg.com.taskmanager.service.RestfulApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestFulApi {
    private final RestfulApiClient restfulApiClient;

    @GetMapping("/objects")
    @ResponseStatus(HttpStatus.OK)
    public String fetchAndLogObjects() {
        restfulApiClient.logExternalDataInfo();
        return "External objects fetched and logged successfully.";
    }
}
