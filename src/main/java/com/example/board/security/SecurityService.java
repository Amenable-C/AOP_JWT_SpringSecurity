package com.example.board.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class SecurityService {
    // 원래는 이렇게 secret key를 만드는게 아니라, 유저의 비밀번호를 이용해서 만드는 형식 등을 이용해서 만들어야 함
    // 이거는 예제용
    // 로그인 서비스 던질때 같이 사용
    private static final String SECRET_KEY = "asdfasasdfasdfasdfdfascasdfasdfasdfasdfasdfasdfasdfasdfasdfvsasdfasdf";

    public String createToken(String subject, long expTime){
        if(expTime <= 0){
            throw new RuntimeException("만료시간이 0보다 커야합니다.");
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject(subject)
                .signWith(signingKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .compact();
    }

    // 원래는 인증되어있는건지 아닌지를 boolean으로 만들어야겠지만, 확인을 위해 해석해서 꺼내오는거 만들기.
    // 밑의 로직을 이용하여 토큰을 검증하는 메서드를 만들어서 boolean으로 검증하기.
    public String getSubject(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


}
