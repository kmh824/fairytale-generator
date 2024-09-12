package com.fairytale.fairytale_generator.service;


import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // OAuth2UserRequest에서 OAuth2UserService를 통해 사용자 정보 가져오기
        OAuth2User oAuth2User = new org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService().loadUser(userRequest);

        // 사용자 정보 처리
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");

        // 권한 설정 (ROLE_USER)
        GrantedAuthority authority = new OAuth2UserAuthority("ROLE_USER", attributes);

        return new DefaultOAuth2User(Collections.singleton(authority), attributes, "email");
    }
}
