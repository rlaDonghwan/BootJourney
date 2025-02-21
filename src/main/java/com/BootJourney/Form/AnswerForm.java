package com.BootJourney.Form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AnswerForm {
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
}