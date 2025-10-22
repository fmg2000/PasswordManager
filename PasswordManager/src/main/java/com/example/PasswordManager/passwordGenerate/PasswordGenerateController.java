package com.example.PasswordManager.passwordGenerate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("generate")
@CrossOrigin
public class PasswordGenerateController {

    @GetMapping("/password")
    public ResponseEntity<String> passwordGenerate(){
        return ResponseEntity.ok().body(PasswordGenerate.generate());
    }

}
