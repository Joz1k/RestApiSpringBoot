package org.example.restapi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class RestApiController {
    private final String baseURL = "https://jsonplaceholder.typicode.com";
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyСacheManager cacheManager;

    public RestApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{path:[a-z]+}")
    public ResponseEntity<String> getProxy(@PathVariable String path, @RequestBody(required = false) String body) {
        String cache = cacheManager.get(path);
        if (cache != null) {
            return ResponseEntity.ok(cache);
        } else {
            String url = baseURL + "/" + path;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(body), String.class);
            cacheManager.put(path, response.getBody());
            return response;
        }
    }

    @GetMapping("/{path:[a-z]+}/{id}")
    public ResponseEntity<String> getProxyById(@PathVariable String path, @PathVariable String id, @RequestBody(required = false) String body) {
        String cache = cacheManager.get(path + "/" + id);
        if (cache != null) {
            return ResponseEntity.ok(cache);
        } else {
            String url = baseURL + "/" + path + "/" + id;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(body), String.class);
            cacheManager.put(path + "/" + id, response.getBody());
            return response;
        }
    }

    @PostMapping("/{path:[a-z]+}")
    public ResponseEntity<String> postProxy(@PathVariable String path, @RequestBody(required = false) String body) {
        String url = baseURL + "/" + path;
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), String.class);
    }

    @PostMapping("/{path:[a-z]+}/{id}")
    public ResponseEntity<String> postProxyById(@PathVariable String path, @PathVariable String id, @RequestBody(required = false) String body) {
        String url = baseURL + "/" + path + "/" + id;
        System.out.println(url);
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), String.class);
    }


    @PutMapping("/{path:[a-z]+}/{id}")
    public ResponseEntity<String> putProxy(@PathVariable String path, @PathVariable String id, @RequestBody String body) {
        String url = baseURL + "/" + path + "/" + id;
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body), String.class);
    }

    @DeleteMapping("/{path:[a-z]+}/{id}")
    public ResponseEntity<String> deleteProxy(@PathVariable String path, @PathVariable String id) {
        String url = baseURL + "/" + path + "/" + id;
        return restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
    }
}
