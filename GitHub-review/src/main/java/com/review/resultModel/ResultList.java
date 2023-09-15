package com.review.resultModel;

import com.review.model.Repo;
import lombok.Data;

import java.util.List;

@Data
public class ResultList implements Result {
    private List<Repo> repoList;

    public ResultList(List<Repo> repoList) {
        this.repoList = repoList;
    }

}
