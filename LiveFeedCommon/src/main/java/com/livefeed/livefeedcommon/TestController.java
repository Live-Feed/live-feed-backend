package com.livefeed.livefeedcommon;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class TestController {

    private final TestKafka testKafka;

    @GetMapping
    public String testKafkaController() {
        testKafka.send();
        return "hello";
    }
}
