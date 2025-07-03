# Проект 2-го спринта
![Apache Kafka](https://img.shields.io/badge/-Apache_Kafka-000?logo=apachekafka)
![Kafka Streams](https://img.shields.io/badge/-Kafka_Streams-000?logo=apachekafka)
![Spring Boot](https://img.shields.io/badge/-Spring_Boot-6DB33F?logo=springboot)

### Описание
Репозиторий предназначен для сдачи проекта 2-го спринта

### Структура репозитория
- `src/main/java/ru/practicum/config` - конфигурации консюмеров, продюсеров, потоков
- `src/main/java/ru/practicum/controller` - REST API для взаимодействия с приложением
- `src/main/java/ru/practicum/service` - бизнес логика приложения
- `src/main/java/ru/practicum/serialization` - сериализация и десериализация объектов
- `src/main/resources/ksqldb-queries.sql` - реализация потоковой обработки сообщений ksqlDB

### Как запустить контейнер
Сборка толстого jar файла:

```
gradlew clean kafka-streams:bootJar
```

Запустите локально Docker:

```shell
cd infra; docker-compose up -d
```

### Как протестировать работу
#### Цензурирование сообщения
Отправить POST запрос на цензурирование слов `http://localhost:9090/messages/block/words`
```json
{
    "recipientId": "7c36d339-8e8b-4de6-8286-ad4a76e14042",
    "words" : [
        "Спам",
        "Мат"
    ]
}
```

Отправить POST запрос на отправку сообщения `http://localhost:9090/messages`
```json
{
    "id": "7c36d339-8e8b-4de6-8286-ad4a76e1404a",
    "sender": {
        "id": "7c36d339-8e8b-4de6-8286-ad4a76e14041"
    },
    "recipient": {
        "id": "7c36d339-8e8b-4de6-8286-ad4a76e14042"
    },
    "message": "Спам звонки, должны блокировать пользователей. Мат исключен.",
    "createDt": "2025-02-10 14:03:35"
}
```

Ожидаемый результат, в топик filtered-messages сообщение попадет с цензурой слов.
```json
{
  "id": "7c36d339-8e8b-4de6-8286-ad4a76e1404a",
  "sender": {
    "id": "7c36d339-8e8b-4de6-8286-ad4a76e14041"
  },
  "recipient": {
    "id": "7c36d339-8e8b-4de6-8286-ad4a76e14042"
  },
  "message": "**** звонки, должны блокировать пользователей. *** исключен.",
  "createDt": "2025-02-10 14:03:35"
}
```

#### Блокировка нежелательных пользователей
Отправить POST запрос на блокировку пользователей `http://localhost:9090/messages/block/users`

```json
{
    "recipientId": "6c36d339-8e8b-4de6-8286-ad4a76e14042",
    "users" : [
        {
            "id": "1c36d339-8e8b-4de6-8286-ad4a76e14041"
        },
        {
            "id": "7c36d339-8e8b-4de6-8286-ad4a76e14048"
        },
        {
            "id": "7c36d339-8e8b-4de6-8286-ad4a76e14049"
        }
    ]
}
```

Отправить POST запрос на отправку сообщения `http://localhost:9090/messages`

```json
{
    "id": "7c36d339-8e8b-4de6-8286-ad4a76e1404a",
    "sender": {
        "id": "1c36d339-8e8b-4de6-8286-ad4a76e14041"
    },
    "recipient": {
        "id": "6c36d339-8e8b-4de6-8286-ad4a76e14042"
    },
    "message": "Спам звонки, должны блокировать пользователей. Мат исключен.",
    "createDt": "2025-02-10 14:03:35"
}
```

Ожидаемый результат, в топик filtered-messages сообщение не попадет из за настройки фильтра.