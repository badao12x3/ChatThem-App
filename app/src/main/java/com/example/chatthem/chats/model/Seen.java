package com.example.chatthem.chats.model;

import java.io.Serializable;
import java.util.List;

public class Seen implements Serializable {
    private String memberId, seen;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }
}
