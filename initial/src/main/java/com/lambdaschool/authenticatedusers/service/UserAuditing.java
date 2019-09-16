package com.lambdaschool.authenticatedusers.service;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component                                        //how we assign the name
public class UserAuditing implements AuditorAware<String>
{

    @Override
    public Optional<String> getCurrentAuditor()
    {
        String uname = "SYSTEM"; // If we don't have user authentication enabled we can change "SYSTEM" to whatever we want
        return Optional.of(uname);
    }

}

// Boilerplate code that's added into service