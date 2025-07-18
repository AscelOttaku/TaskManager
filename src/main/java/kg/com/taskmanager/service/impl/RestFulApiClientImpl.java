package kg.com.taskmanager.service.impl;

import kg.com.taskmanager.dto.ObjectDto;
import kg.com.taskmanager.service.RestfulApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestFulApiClientImpl implements RestfulApiClient {
    private final RestClient restClient;

    @Override
    public void logExternalDataInfo() {
        List<ObjectDto> objectDtos = restClient.get()
                .uri("objects")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ObjectDto>>() {});

        if (objectDtos == null || objectDtos.isEmpty()) {
            log.warn("No data found in external API");
            return;
        }

        objectDtos.forEach(objectDto -> {
            log.info("Objects: ID: {}, Name: {}",
                    objectDto.getId(),
                    objectDto.getName()
            );

            if (objectDto.getData() == null || objectDto.getData().isEmpty()) {
                log.info("No data available for object with ID: {}", objectDto.getId());
                return;
            }
            objectDto.getData()
                    .forEach((key, value) -> log.info("Data: Key: {}, Value: {}", key, value));
        });
    }
}
