package org.haupt.chemicals.api.service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.*;
import org.haupt.chemicals.api.model.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MailJetTemplate {
    /**
     * This call sends a message to the given recipient with vars and custom vars.
     */
    public static void mailTemplate(String email, String name, List<Product> products, String apikey, String apiSecret) throws MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(ClientOptions.builder().apiKey(apikey).apiSecretKey(apiSecret).build());
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", email)
                                        .put("Name", name))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", name)))
                                .put(Emailv31.Message.TEMPLATEID, 4558978)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT, "Bestellbestätigung")
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("Produkt", products))));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
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
