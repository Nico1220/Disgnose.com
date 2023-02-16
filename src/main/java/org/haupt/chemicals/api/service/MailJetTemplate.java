package org.haupt.chemicals.api.service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import com.mailjet.client.resource.Listrecipient;
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
                                        .put("Email", "wro19770@spengergasse.at")
                                        .put("Name", "Ala"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", name)))
                                .put(Emailv31.Message.TEMPLATEID, 4558978)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT, "")
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("Produkt", products))));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }

    public static void mailVertifizierung(String email, String name, String apikey, String apiSecret) throws MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(ClientOptions.builder().apiKey(apikey).apiSecretKey(apiSecret).build());
        request = new MailjetRequest(Listrecipient.resource)
                .property(Listrecipient.ISUNSUBSCRIBED, "true")
                .property(Listrecipient.CONTACTEMAIL, email)
                .property(Listrecipient.CONTACT, name)
                .property(Listrecipient.LIST, "MyFirstTest");
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
}
