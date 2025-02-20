package com.GDG.Festi.domain.polaroid.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateResponseDTO {
    private Long polaroidId;
    private String userId;
    private String imgLink;
}
