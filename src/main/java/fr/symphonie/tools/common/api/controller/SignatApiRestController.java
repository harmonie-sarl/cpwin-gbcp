package fr.symphonie.tools.common.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.symphonie.tools.common.api.model.EmailDetails;
import fr.symphonie.tools.common.api.services.EmailService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class SignatApiRestController {
	@Autowired
	private EmailService emailService;
	 
    // Sending a simple Email
	@RequestMapping(value="/sendMail",method = RequestMethod.POST,produces =MediaType.APPLICATION_JSON_VALUE)
    public String sendMail(@RequestBody EmailDetails details)
    {
        String status
            = emailService.sendSimpleMail(details);
 
        return status;
    }
 
    // Sending email with attachment
    @RequestMapping(value="/sendMailWithAttachment",method = RequestMethod.POST,produces =MediaType.APPLICATION_JSON_VALUE)
    public String sendMailWithAttachment(
        @RequestBody EmailDetails details)
    {
        String status
            = emailService.sendMailWithAttachment(details);
 
        return status;
    }

}
