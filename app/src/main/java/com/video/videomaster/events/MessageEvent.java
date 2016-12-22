package com.video.videomaster.events;


public class MessageEvent {
    int position;

    public MessageEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

