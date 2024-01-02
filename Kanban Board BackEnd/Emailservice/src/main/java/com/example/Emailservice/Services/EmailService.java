package com.example.Emailservice.Services;

import com.example.Emailservice.Domain.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);


    String sendMailWithAttachment(EmailDetails details);
}
