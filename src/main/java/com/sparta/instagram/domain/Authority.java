package com.sparta.instagram.domain;


public enum Authority {

    ROLE_USER, ROLE_ADMIN

}
//public enum UserRoleEnum {
//
//    //    ROLE_USER,
////    ROLE_ADMIN;
//    USER(Authority.USER),
//    ADMIN(Authority.ADMIN);
//
//    private final String authority;
//
//    UserRoleEnum(String authority){
//        this.authority = authority;
//    }
//
//    public String getAuthority(){
//        return this.authority;
//    }
//
//    public static class Authority{
//        public static final String USER = "ROLE_USER";
//        public static final String ADMIN = "ROLE_ADMIN";
//    }