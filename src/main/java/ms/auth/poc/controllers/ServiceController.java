package ms.auth.poc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class ServiceController {
    @RequestMapping("/")
    public Map success() {
        return Collections.singletonMap("isWorking", true);
    }
}
