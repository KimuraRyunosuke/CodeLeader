package com.example.codeleader;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import java.util.ArrayList;


public class PostData {
    @NotBlank(message = "タイトルを入力してください")
    private String title;
    @NotBlank(message = "コメントを入力してください")
    private String comment;
    @NotEmpty(message = "コードのURLを入力してください")
    private List<@Valid @Pattern(regexp = "^https:\\/\\/github\\.com\\/.*", message = "https://github.com/で始まるURLを入力してください") @NotBlank(message = "空の入力欄がありました,もう一ど入力してください") String> codeList;

    public PostData() {
        codeList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getCodeList() {
        return codeList;
    }
    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }
}
