package com.enoca.enocaTask.security;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;


}
