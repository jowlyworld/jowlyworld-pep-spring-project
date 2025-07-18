package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Optional<Message> createMessage(Message message, boolean userExists) {
        if (!userExists ||
            message.getMessageText() == null || message.getMessageText().isBlank() ||
            message.getMessageText().length() > 255) {
            return Optional.empty();
        }
        return Optional.of(messageRepository.save(message));
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public boolean deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    public boolean updateMessageText(Integer messageId, String newText) {
        Optional<Message> optional = messageRepository.findById(messageId);
        if (optional.isPresent() && newText != null && !newText.isBlank() && newText.length() <= 255) {
            Message message = optional.get();
            message.setMessageText(newText);
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
