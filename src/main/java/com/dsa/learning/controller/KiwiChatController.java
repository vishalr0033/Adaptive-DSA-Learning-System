package com.dsa.learning.controller;

import com.dsa.learning.dto.KiwiChatRequestDto;
import com.dsa.learning.dto.KiwiChatResponseDto;
import com.dsa.learning.service.ai.KiwiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kiwi")
@CrossOrigin(origins = "*")
public class KiwiChatController {

    private final KiwiChatService kiwiChatService;

    @Autowired
    public KiwiChatController(KiwiChatService kiwiChatService) {
        this.kiwiChatService = kiwiChatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<KiwiChatResponseDto> chatWithKiwi(@RequestBody KiwiChatRequestDto request) {
        KiwiChatResponseDto response = kiwiChatService.chat(request);
        return ResponseEntity.ok(response);
    }
}
