package com.hyunmin.gpt.domain.account.repository;

import com.hyunmin.gpt.domain.account.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
