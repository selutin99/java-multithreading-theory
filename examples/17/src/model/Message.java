package model;

import java.util.Objects;

public class Message {

    private String data;

    public Message(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(data, message.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "Message{" +
                "data='" + data + '\'' +
                '}';
    }
}
