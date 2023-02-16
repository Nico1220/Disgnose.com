package org.haupt.chemicals.api.service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.*;
import com.mailjet.client.transactional.*;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import org.haupt.chemicals.api.model.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class MailJetTemplate {
    /**
     * This call sends a message to the given recipient with vars and custom vars.
     */
    public static void mailTemplate(String email, String name, List<Product> products, String apikey, String apiSecret) throws MailjetException {
//        MailjetClient client;
//        MailjetRequest request;
//        MailjetResponse response;
//        client = new MailjetClient(ClientOptions.builder().apiKey(apikey).apiSecretKey(apiSecret).build());
//        request = new MailjetRequest(Emailv31.resource)
//                .property(Emailv31.MESSAGES, new JSONArray()
//                        .put(new JSONObject()
//                                .put(Emailv31.Message.FROM, new JSONObject()
//                                        .put("Email", email)
//                                        .put("Name", name))
//                                .put(Emailv31.Message.TO, new JSONArray()
//                                        .put(new JSONObject()
//                                                .put("Email", email)
//                                                .put("Name", name)))
//                                .put(Emailv31.Message.TEMPLATEID, 4558978)
//                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
//                                .put(Emailv31.Message.SUBJECT, "Bestellbestätigung")
//                                .put(Emailv31.Message.VARIABLES, new JSONObject()
//                                        .put("Produkt", products))));
//        response = client.post(request);
//        System.out.println(response.getStatus());
//        System.out.println(response.getData());

        ClientOptions options = ClientOptions.builder()
                .apiKey(apikey)
                .apiSecretKey(apiSecret)
                .build();

        MailjetClient client = new MailjetClient(options);

        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(email, name))
                .from(new SendContact(email, email))
                .htmlPart("<h1>This is the HTML content of the mail</h1>")
                .subject("Bestellbestätigung")
                .trackOpens(TrackOpens.ENABLED)
                .templateID(Long.valueOf(4558978))
                .variable("Produkt", products)
                .header("test-header-key", "test-value")
                .customID("custom-id-value")
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message1) // you can add up to 50 messages per request
                .build();

        // act
        SendEmailsResponse response = request.sendWith(client);
        System.out.println(response);

    }

    public static void mailVertifizierung(String email, String name, String apikey, String apiSecret) throws MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetRequest request1;
        MailjetResponse response;
        client = new MailjetClient(ClientOptions.builder().apiKey(apikey).apiSecretKey(apiSecret).build());
        request = new MailjetRequest(Contact.resource)
                .property(Contact.EMAIL, email)
                .property(Contact.NAME, name);
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
        request1 = new MailjetRequest(ContactManagemanycontacts.resource)
                .property(ContactManagemanycontacts.CONTACTS, new JSONArray()
                        .put(new JSONObject()
                                .put("Email", email)
                                .put("Name", name)
                        )
                )
                .property(ContactManagemanycontacts.CONTACTSLISTS, new JSONArray()
                        .put(new JSONObject()
                                .put("Action", "addforce")
                                .put("ListID", "32233")
                        )
                );
        response = client.post(request1);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
}
