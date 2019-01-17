package com.mdstech.server.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SampleService {

    private final List<String> messages = new ArrayList<>();

    public String latestMessage() {
        return messages.get(messages.size()-1);
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }
}
