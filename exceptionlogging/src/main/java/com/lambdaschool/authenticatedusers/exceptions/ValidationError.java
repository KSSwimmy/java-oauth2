package com.lambdaschool.authenticatedusers.exceptions;

public class ValidationError
{
    private String code; // the code where the validation failed
    private String message; // and the message

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
