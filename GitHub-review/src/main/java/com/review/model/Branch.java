package com.review.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Branch implements Serializable {
    private String name;
    private String lastCommitSHA;

    public Branch(String name, String lastCommitSHA) {
        this.name = name;
        this.lastCommitSHA = lastCommitSHA;
    }
}
