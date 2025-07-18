package com.example.controller;
import org.springframework.http.ResponseEntity;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public Object register(@RequestBody Account account) {
        Optional<Account> result = accountService.register(account);
        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        } else if (result.get().getAccountId() == null) {
            return ResponseEntity.status(409).body(null); // Username conflict
        }
        return result.get();
    }

    @PostMapping("/login")
    public Object login(@RequestBody Account loginAttempt) {
        Optional<Account> account = accountService.login(loginAttempt.getUsername(), loginAttempt.getPassword());
        if (account.isPresent()) {
            return account.get();
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/messages")
    public Object createMessage(@RequestBody Message message) {
        boolean userExists = accountService.exists(message.getPostedBy());
        Optional<Message> result = messageService.createMessage(message, userExists);
        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        return result.get();
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{messageId}")
    public Object getMessageById(@PathVariable Integer messageId) {
        return messageService.getMessageById(messageId).orElse(null);
    }

    @DeleteMapping("/messages/{messageId}")
    public Object deleteMessage(@PathVariable Integer messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        if (deleted) {
            return 1;
        } else {
            return null;
        }
    }

    @PatchMapping("/messages/{messageId}")
    public Object updateMessage(@PathVariable Integer messageId, @RequestBody Map<String, String> body) {
        String newText = body.get("messageText");
        boolean updated = messageService.updateMessageText(messageId, newText);
        if (updated) {
            return 1;
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable Integer accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }
}