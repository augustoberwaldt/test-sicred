package com.sicredi.app.enumerable;

public enum ExternalApiEnum {

    VALIDATE_CPF_URI("https://user-info.herokuapp.com/users/");

    private String value;

    ExternalApiEnum(String s) {
        this.value = s;
    }

    public String getValue() {
        return  this.value;
    }

}
