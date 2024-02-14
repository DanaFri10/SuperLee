package suppliers.business;

import javax.mail.*;
import java.util.Properties;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static suppliers.business.Utils.*;

public class Contact {
    final private String contactId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public Contact(String contactId, String name, String phoneNumber, String email, String address)
    {
        if(!isLegalId(contactId))
            throw new IllegalArgumentException("Contact ID (" + contactId + ") is illegal.");
        verifyContactInfo(name, phoneNumber, email, address);

        this.contactId = contactId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public void verifyContactInfo(String name, String phoneNumber, String email, String address)
    {
        if(isFieldEmpty(name) | isFieldEmpty(phoneNumber) | isFieldEmpty(email) | isFieldEmpty(address))
            throw new IllegalArgumentException("All fields must not be empty.");
        if(!isLegalName(name))
            throw new IllegalArgumentException(String.format("Contact name (%s) must only contain letters.", name));
        if(!isLegalPhoneNumber(phoneNumber))
            throw new IllegalArgumentException("Contact's phone number (" + phoneNumber + ") is illegal.");
        if(!isLegalEmail(email))
            throw new IllegalArgumentException("Contact's email address (" + email + ") is illegal.");
        if(!isStringLengthBetween(address, 3, 100))
            throw new IllegalArgumentException("Contact location (" + address + ") must include between 3 and 50 characters.");
    }

    public void updateContactInfo(String name, String phoneNumber, String email, String address)
    {
        verifyContactInfo(name, phoneNumber, email, address);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public void sendEmail(String subject, String content) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String fromEmail = "super.lee.bgu1@gmail.com";
        String password = "chudvjpbqugncgye";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject(subject);
        message.setText(content);

        Transport.send(message);
    }

    public String getContactId()
    {
        return contactId;
    }

    public String getName()
    {
        return name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public String getAddress()
    {
        return address;
    }

}
