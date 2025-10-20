# 🚀 Микросервисная система "User & Notification" с Apache Kafka

Добро пожаловать! Этот репозиторий содержит **главный сервис (`user-service`)** и всю необходимую конфигурацию для запуска полной микросервисной системы.

## 🎯 Архитектура и задача

Система демонстрирует асинхронное взаимодействие между двумя независимыми микросервисами для отправки email-уведомлений.

1.  **`user-service` (этот репозиторий):**
    *   Отвечает за CRUD-операции с пользователями.
    *   При создании или удалении пользователя **отправляет событие** в Apache Kafka.
    *   [Репозиторий `user-service` на GitHub](https://github.com/isaakmaz/user-service-spring)

2.  **`notification-service` (независимый сервис):**
    *   **Слушает** события из Kafka.
    *   Отправляет email-уведомления пользователям.
    *   [Репозиторий `notification-service` на GitHub](https://github.com/isaakmaz/notification-service)

---

## 🚀 Как запустить всю систему 

Для запуска всей инфраструктуры (2 сервиса, Kafka, Zookeeper, PostgreSQL) используется **Docker Compose**. Файл `docker-compose.yml` находится в этом репозитории.

### Требования

*   Установленный **Docker Desktop**.

### Инструкция

1.  **Клонируйте только этот репозиторий:**
    ```bash
    git clone https://github.com/isaakmaz/user-service-spring.git
    cd user-service-spring
    ```

2.  **Запустите Docker Compose:**
    Находясь в папке `user-service-spring`, выполните команду:
    ```bash
    docker-compose up --build -d
    ```

**Что произойдет?**
*   Docker Compose запустит Kafka, Zookeeper и PostgreSQL из готовых образов.
*   Он соберет Docker-образ для `user-service` из локальных файлов этого репозитория.
*   Он **автоматически склонирует репозиторий `notification-service` с GitHub** и соберет его Docker-образ.
*   В итоге будут запущены все 5 контейнеров, и система будет готова к работе.

---

## 🧪 Тестовые сценарии для проверки

Используйте Postman или `curl`. Email-уведомления будут перехвачены сервисом **Mailtrap**.

1.  **Создание пользователя (проверка всей цепочки Kafka):**
    *   **Метод:** `POST`
    *   **URL:** `http://localhost:8080/api/users`
    *   **Тело:** `{"name": "Иван", "email": "test@mailtrap.io", "age": 30}`
    *   **Ожидаемый результат:** На Mailtrap приходит приветственное письмо.

2.  **Ручная отправка email (проверка API `notification-service`):**
    *   **Метод:** `POST`
    *   **URL:** `http://localhost:8081/api/notifications/send-email`
    *   **Тело:** `{"to": "manual@test.ru", "subject": "Проверка", "body": "Прямой запрос"}`
    *   **Ожидаемый результат:** На Mailtrap приходит письмо с темой "Проверка".

---

## ✅ Автоматические тесты

Оба проекта содержат независимые интеграционные тесты, которые можно запустить из IDE или командой `mvn clean test` в папке каждого проекта.