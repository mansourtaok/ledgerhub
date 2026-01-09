package com.ledgerhub.service;

import com.ledgerhub.model.dto.JwtRequest;
import com.ledgerhub.model.dto.JwtResponse;

public interface IAuthService {

	JwtResponse authenticate(JwtRequest request) ;
}
