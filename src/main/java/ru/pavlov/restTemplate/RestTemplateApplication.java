package ru.pavlov.restTemplate;

import models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class RestTemplateApplication {

    static RestTemplate restTemplate = new RestTemplate();
    static final String URL = "http://94.198.50.185:7081/api/users";

    static ResponseEntity<String> showUsers(HttpEntity requestEntity) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

//        System.out.println("StatusCode: " + responseEntity.getStatusCode());
//        System.out.println("Headers: " + responseEntity.getHeaders());
//        System.out.println("Body: " + responseEntity.getBody());

        return responseEntity;

    }

    static ResponseEntity<String> showUsers() {

        return showUsers(null);

    }

    static void addUser(HttpEntity requestEntity) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

//        System.out.println("StatusCode: " + responseEntity.getStatusCode());
//        System.out.println("Headers: " + responseEntity.getHeaders());
        System.out.println("Body: " + responseEntity.getBody());

    }

    static void editUser(HttpEntity requestEntity) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

//        System.out.println("StatusCode: " + responseEntity.getStatusCode());
//        System.out.println("Headers: " + responseEntity.getHeaders());
        System.out.println("Body: " + responseEntity.getBody());
    }

    static void deleteUser(HttpEntity requestEntity, long id) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL + "/" + id,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

//        System.out.println("StatusCode: " + responseEntity.getStatusCode());
//        System.out.println("Headers: " + responseEntity.getHeaders());
        System.out.println("Body: " + responseEntity.getBody());
    }

    static HttpHeaders setHeaders(ResponseEntity responseEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
        headers.set("Cookie", cookies.stream().collect(Collectors.joining(";")));

        return headers;
    }


    public static void main(String[] args) {
        SpringApplication.run(RestTemplateApplication.class, args);
        System.out.println("appStarted");

        // Запрашиваем всех пользователей и заполняем хедеры
        ResponseEntity<String> responseEntityAll = showUsers();
        HttpHeaders headers = setHeaders(responseEntityAll);

        // Создаем пользователя с id = 3
        User user = new User();
        user.setId((long) 3);
        user.setName("James");
        user.setLastName("Brown");
        user.setAge((byte) 29);

        // Добавляем
        addUser(new HttpEntity<>(user, headers));

        // Проверяем
        showUsers(new HttpEntity<>(headers));

        // Изменяем пользователя с id = 3
        user.setName("Thomas");
        user.setLastName("Shelby");

        // Изменяем
        editUser(new HttpEntity<>(user, headers));

        // Проверяем
        showUsers(new HttpEntity<>(headers));

        // Удаляем
        deleteUser(new HttpEntity<>(headers), user.getId());

        // Проверяем
        showUsers(new HttpEntity<>(headers));

    }

}
