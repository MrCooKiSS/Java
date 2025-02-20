# Используем официальный образ OpenJDK в качестве базового образа
FROM openjdk:23-ea-jdk

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /demo

# Копируем файл с зависимостями (если используется Maven)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Собираем проект с помощью Maven
RUN ./mvnw clean package -DskipTests

# Указываем порт, который будет использовать приложение
EXPOSE 8000

# Команда для запуска приложения
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]