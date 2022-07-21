package kr.wiselight.metaverse.backend.constant;

public class JwtConstant {

    public static final String EXCEPTION_HEADER = "exception";
    public static final String BEARER_PREFIX = "Bearer ";


    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
}
