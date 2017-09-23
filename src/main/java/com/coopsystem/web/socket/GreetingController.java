package com.coopsystem.web.socket;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by Dariusz Ł on 12.05.2017.
 */

@Controller
public class GreetingController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated dela y
        System.out.print("jes");
        return new Greeting("Hello, " + message.getName() + "!");
    }


}
