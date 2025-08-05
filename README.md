# ✅ Task Management REST API

Простое приложение для управления задачами, разработанное на Spring Boot. В проекте реализован CRUD-интерфейс для работы
с задачами, подключена база данных H2 и выполнены дополнительные технические требования.

---

## 📌 Основные требования (обязательные)

- ✅ Создание задачи: `POST /api/tasks`
- ✅ Получение всех задач: `GET /api/tasks`
- ✅ Получение задачи по ID: `GET /api/tasks/{id}`
- ✅ Обновление задачи: `PUT /api/tasks/{id}`
- ✅ Удаление задачи: `DELETE /api/tasks/{id}`
- ✅ Подключение базы данных H2
- ✅ Логирование HTTP-запросов и ответов (метод, URI, тело запроса/ответа)

---

## ➕ Дополнительные задания (выполнено +6 баллов)

- ✅ GET-запрос на внешний API: `https://api.restful-api.dev/objects` с логированием
- ✅ Unit-тесты для CRUD-методов `TaskService`
- ✅ Отправка email при создании задачи
- ✅ Basic Authentication защита API
- ✅ Кэширование `GET /api/tasks` с использованием Spring Cache (`@Cacheable`)
- ✅ Dockerfile для контейнеризации приложения
- ⛔ Redis не использовался (заменён на Spring Cache)

---

## 🛠️ Технологии

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- SLF4J / Logback
- (дополнительно: JavaMail, Spring Security, Redis, Docker)

---

## 🚀 Как запустить

1. Склонируйте репозиторий
2. Запустите через IDE (или используйте `./mvnw spring-boot:run`)
3. Перейдите по адресу: `http://localhost:8080`

---

## 🧪 Как запускать тесты

```bash
./mvnw test
