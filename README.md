# Human Simulation Starter

**Стартер-библиотека** для создания системы управления "андроидами" с возможностью приёма команд, аудита действий и мониторинга производительности.

**⚠️ Важно:** Это стартер-библиотека, а не самостоятельное приложение. Для использования необходимо создать отдельное приложение.

## 🚀 Возможности стартера

### 📋 Основной функционал
- **Приём и исполнение команд** через REST API
- **Приоритизация команд** (CRITICAL - мгновенно, COMMON - в очередь)
- **Аудит всех действий** с возможностью отправки в Kafka или вывода в консоль
- **Мониторинг занятости** и эффективности работы
- **Ограниченная очередь команд** с обработкой переполнения

### 🔧 Технические возможности
- **ThreadPoolExecutor** для эффективной обработки очереди задач
- **Spring Boot Actuator** и **Micrometer** для метрик
- **Kafka интеграция** для отправки аудита

## 📦 Установка и использование стартера

### Предварительные требования
- Java 21+
- Gradle 8+ или Maven
- Kafka (опционально, для аудита)

### 1. Сборка стартера
```bash
git clone <repository-url>
cd Human-simulation-starter
./gradlew clean build publishToMavenLocal
```

### 2. Использование в вашем проекте

#### Добавьте зависимость в build.gradle.kts:
```kotlin
dependencies {
    implementation("org.project:human-simulation-starter:1.0-SNAPSHOT")
}
```

#### Или в pom.xml:
```xml
<dependency>
    <groupId>org.project</groupId>
    <artifactId>human-simulation-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 3. Создание приложения с main классом

Создайте отдельное приложение, которое будет использовать стартер:

```java
@SpringBootApplication
@EnableAspectJAutoProxy
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### 4. Запуск вашего приложения
```bash
./gradlew bootRun
```

## 🎯 Использование стартера

### Аннотация для аудита
Используйте аннотацию `@WeylandWatchingYou` для автоматического аудита методов:

```java
@WeylandWatchingYou(auditMode = AuditMode.CONSOLE)
public void someMethod() {
    // Ваш код
}

@WeylandWatchingYou(auditMode = AuditMode.KAFKA, kafkaTopic = "audit-topic")
public void anotherMethod() {
    // Ваш код
}
```

### Создание REST контроллера
```java
@RestController
@RequestMapping("/api/commands")
public class CommandController {
    
    @Autowired
    private CommandHandlerService commandHandlerService;
    
    @PostMapping
    public ResponseEntity<String> handleCommand(@RequestBody Command command) {
        commandHandlerService.handleCommand(command);
        return ResponseEntity.ok("Command received");
    }
}
```

### Отправка команд
```bash
# CRITICAL команда (выполняется мгновенно)
curl -X POST http://localhost:8081/api/commands \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Critical task",
    "priority": "CRITICAL",
    "author": "user1"
  }'

# COMMON команда (добавляется в очередь)
curl -X POST http://localhost:8081/api/commands \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Regular task",
    "priority": "COMMON",
    "author": "user2"
  }'
```

## 📊 Мониторинг и метрики

### Actuator endpoints
- `http://localhost:8081/actuator/health` - состояние приложения
- `http://localhost:8081/actuator/metrics` - все метрики
- `http://localhost:8081/actuator/prometheus` - метрики в формате Prometheus

### REST API для метрик
- `http://localhost:8081/api/metrics/queue-size` - размер очереди
- `http://localhost:8081/api/metrics/completed-tasks` - выполненные задачи по авторам
- `http://localhost:8081/api/metrics/summary` - сводка всех метрик

### Доступные метрики
- `android.queue.size` - текущий размер очереди команд
- `android.completed.tasks` - количество выполненных задач по авторам

## ⚙️ Конфигурация

### application.properties
```properties
# Server
server.port=8081

# Actuator
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always

# Kafka (опционально)
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
```

### Настройка ThreadPoolExecutor
В `CommandHandlerService` можно настроить:
- **Core Pool Size**: 2 потока (минимальное количество)
- **Maximum Pool Size**: 5 потоков (максимальное количество)
- **Queue Size**: 100 задач (ограничение очереди)
- **Keep Alive Time**: 60 секунд

## 🔍 Архитектура

### Основные компоненты
- **CommandHandlerService** - обработка команд с ThreadPoolExecutor
- **AuditAspect** - AOP для автоматического аудита
- **AuditSenderKafkaService** - отправка аудита в Kafka/консоль
- **MetricsService** - сбор и публикация метрик
- **WorkloadMonitorService** - мониторинг занятости
- **GlobalExceptionHandler** - глобальная обработка исключений



## 🚨 Обработка ошибок

### HTTP коды ошибок
- **400 Bad Request** - некорректные данные
- **503 Service Unavailable** - переполнение очереди команд
- **500 Internal Server Error** - внутренние ошибки

### Пример ответа при ошибке
```json
{
  "error": "Command queue is full",
  "timestamp": 1752992448984,
  "type": "QUEUE_OVERFLOW"
}
```


### Расширение аудита
```java
@WeylandWatchingYou(auditMode = AuditMode.KAFKA, kafkaTopic = "custom-topic")
public void customMethod() {
    // Ваш код с автоматическим аудитом
}
```

## 📝 Примеры использования

### Полный цикл работы
1. **Отправка команды** → REST API
2. **Обработка** → ThreadPoolExecutor
3. **Аудит** → автоматически через AOP
4. **Метрики** → обновление статистики
5. **Мониторинг** → через Actuator/REST API

### Пример полного приложения
Создайте отдельное приложение, которое использует стартер:

```java
@SpringBootApplication
@EnableAspectJAutoProxy
public class BishopPrototype {
    public static void main(String[] args) {
        SpringApplication.run(BishopPrototype.class, args);
    }
}

@RestController
public class CommandController {
    @Autowired
    private CommandHandlerService commandHandlerService;
    
    @PostMapping("/api/commands")
    public ResponseEntity<String> handleCommand(@RequestBody Command command) {
        commandHandlerService.handleCommand(command);
        return ResponseEntity.ok("Command processed");
    }
}
```

### Интеграция с внешними системами
- **Prometheus** → `/actuator/prometheus`
- **Kafka** → аудит сообщений
- **Grafana** → визуализация метрик

## 📄 Лицензия

Этот проект лицензирован под MIT License.
