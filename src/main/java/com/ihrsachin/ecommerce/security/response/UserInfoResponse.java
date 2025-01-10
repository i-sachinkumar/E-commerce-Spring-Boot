package com.ihrsachin.ecommerce.security.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Builder
@Getter
@Setter
public class UserInfoResponse {
    private long id;
    private String jwtToken;
    private String username;
    private List<String> roles;
}
