package kg.com.taskmanager.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kg.com.taskmanager.dto.TaskDto;
import kg.com.taskmanager.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(statements = {
        "INSERT INTO ROLE(ROLE_NAME) VALUES ('ROLE_USER')",
        "INSERT INTO users (NAME, password, EMAIL, ROLE_ID) VALUES ('TestUser', 'password', 'testUser@gmail.com', (" +
                "SELECT R.ID FROM ROLE R WHERE ROLE_NAME = 'ROLE_USER'))",
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithUserDetails("testUser@gmail.com")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDto taskDto;
    private TaskDto invalidTaskDto;

    @BeforeEach
    void setUp() {
        taskDto = TaskDto.builder()
                .name("Test Task")
                .description("This is a test task")
                .build();

        invalidTaskDto = TaskDto.builder()
                .name("")
                .description("This is a test task")
                .userDto(null)
                .build();
    }

    @Test
    void createTask() throws Exception {
        var serializedTaskDto = objectMapper.writeValueAsString(taskDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedTaskDto))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Task"))
                .andExpect(jsonPath("$.description").value("This is a test task"))
                .andExpect(jsonPath("$.taskStatus").value("NEW"))
                .andExpect(jsonPath("$.userDto.name").value("TestUser"));
    }

    @Test
    void createTaskWithInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTaskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask() throws Exception {
        TaskDto updatedTask = createTestTaskDto(MockMvcRequestBuilders.post("/api/tasks"));
        updatedTask.setName("Updated Task");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Task"));
    }

    private TaskDto createTestTaskDto(MockHttpServletRequestBuilder post) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                TaskDto.class
        );
    }

    @Test
    void updateNonExistingTask() throws Exception {
        TaskDto updatedTask = createTestTaskDto(MockMvcRequestBuilders.put("/api/tasks"));

        updatedTask.setId(updatedTask.getId() + 1); // Simulating an invalid ID
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTaskById() throws Exception {
        Long createdTaskId = createTestTaskDto(MockMvcRequestBuilders.post("/api/tasks"))
                .getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/" + createdTaskId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Task"))
                .andExpect(jsonPath("$.description").value("This is a test task"))
                .andExpect(jsonPath("$.taskStatus").value("NEW"));
    }

    @Test
    void updateTaskStatus() throws Exception {
        Long createdTaskId = createTestTaskDto(MockMvcRequestBuilders.post("/api/tasks"))
                .getId();

        String newStatus = "IN_PROGRESS";

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/tasks/" + createdTaskId + "/status")
                        .param("status", newStatus))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskStatus").value(TaskStatus.IN_PROGRESS));
    }

    @Test
    void findAllTasks() throws Exception {
        var taskDto1 = createTestTaskDto(MockMvcRequestBuilders.post("/api/tasks"));
        var taskDto2 = createTestTaskDto(MockMvcRequestBuilders.post("/api/tasks"));
        var taskDto3 = createTestTaskDto(MockMvcRequestBuilders.post("/api/tasks"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].id").value(taskDto1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(taskDto2.getId()))
                .andExpect(jsonPath("$.content[2].id").value(taskDto3.getId()));
    }

    @Test
    void findTaskById() throws Exception {
        var taskDto1 = createTestTaskDto(MockMvcRequestBuilders.post("/api/tasks"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/" + taskDto1.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskDto1.getId()))
                .andExpect(jsonPath("$.name").value(taskDto1.getName()))
                .andExpect(jsonPath("$.description").value(taskDto1.getDescription()))
                .andExpect(jsonPath("$.taskStatus").value(taskDto1.getTaskStatus()));
    }
}