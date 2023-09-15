package com.review.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class Repo implements Serializable {

    private String name;
    private String ownerLogin;
    private List<Branch> branches;

    public Repo(String name, String ownerLogin, List<Branch> branches) {
        this.name = name;
        this.ownerLogin = ownerLogin;
        this.branches = branches;
    }
}
