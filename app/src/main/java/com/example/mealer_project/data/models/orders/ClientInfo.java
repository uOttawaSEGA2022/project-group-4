package com.example.mealer_project.data.models.orders;

import com.example.mealer_project.data.models.Client;

import java.io.Serializable;

public class ClientInfo implements Serializable {
    String clientId;
    String clientName;
    String clientEmail;

    public ClientInfo(String clientId, String clientName, String clientEmail) {
        this.setClientId(clientId);
        this.setClientName(clientName);
        this.setClientEmail(clientEmail);
    }

    public ClientInfo(Client client) {
        this.setClientId(client.getUserId());
        this.setClientName(client.getFirstName() + " " + client.getLastName());
        this.setClientEmail(client.getEmail());
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}
