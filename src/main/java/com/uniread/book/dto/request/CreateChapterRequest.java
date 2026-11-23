package com.uniread.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateChapterRequest {

    @NotBlank(message = "Chapter title must not be empty")
    private String title;
    private Integer chapterNumber;

}
