package com.example.codeleader;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PostData {
    @NotBlank(message = "タイトルを入力してください")
    private String title;
    @NotBlank(message = "コメントを入力してください")
    private String comment;
    @NotEmpty(message = "コードのURLを入力してください")
    private List<@Valid @Pattern(regexp = "^https:\\/\\/github\\.com\\/.*", message = "https://github.com/で始まるURLを入力してください") @NotBlank(message = "空の入力欄がありました,もう一ど入力してください") String> codeList;

}
