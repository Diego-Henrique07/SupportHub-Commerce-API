package supporthub_commerce_api.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/protected")
    public ResponseEntity<String> protectedRoute(){
        return ResponseEntity.ok("Protected endpoint accessed successfully");
    }
}
