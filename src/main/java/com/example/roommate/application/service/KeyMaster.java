package com.example.roommate.application.service;

import java.util.UUID;

public record KeyMaster(
        UUID userKey, UUID roomKey, String owner, String roomName) {
    @Override
    public String toString() {
        return "KeyMaster{"
                +
                "userKey="
                + userKey
                +
                ", roomKey="
                + roomKey
                +
                ", owner='"
                + owner + '\''
                +
                ", roomName='" + roomName + '\''
                +
                '}';
    }
}
