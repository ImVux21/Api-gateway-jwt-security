package org.vux.userservice.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResponse {
    private String message;
    private Object data;
    private int status;
}
