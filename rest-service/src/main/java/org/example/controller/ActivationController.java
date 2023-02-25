package org.example.controller;

import org.example.service.UserActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class ActivationController {
    private final UserActivationService userActivationService;

    public ActivationController(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }
    @RequestMapping(method = RequestMethod.GET, value = "/activation")
    public ResponseEntity<?> activation (@RequestParam("id") String id){
        var res = userActivationService.activation(id);
        if(res){
            return ResponseEntity.ok().body("Registration successful is finish!");
        }
        return ResponseEntity.internalServerError().build();
    }

}
