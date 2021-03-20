package io.github.hhservers.bclanchat.util.objects;

import java.util.UUID;

public class Request {

    public Request(UUID id, UUID sender, UUID receiver, Boolean acknowledged){
        this.id=id;
        this.sender=sender;
        this.receiver=receiver;
        this.acknowledged=acknowledged;
    }

    private final UUID id;
    private UUID sender;
    private UUID receiver;
    private Boolean acknowledged;

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public void setReceiver(UUID receiver) {
        this.receiver = receiver;
    }

    public Boolean getAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(Boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

}
