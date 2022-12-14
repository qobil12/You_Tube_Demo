package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {
    private String id;
    private String originalName;
    private String extension;
    private String path;
    private Long size;
    private AttachDTO image;
    private LocalDateTime createdDate;

    private String url;

    public AttachDTO(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
