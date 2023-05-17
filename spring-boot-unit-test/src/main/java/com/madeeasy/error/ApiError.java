package com.madeeasy.error;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    private String message;
    private String details;
    private String hint;
    private String nextActions;
    private String support;
}
