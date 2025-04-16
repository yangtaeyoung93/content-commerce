package com.commerce.content.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TagsViewResponse {
    private Long id;
    private String name;

    @Builder
    public TagsViewResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
