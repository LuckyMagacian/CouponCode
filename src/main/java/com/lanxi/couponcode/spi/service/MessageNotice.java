package com.lanxi.couponcode.spi.service;

import java.util.Collection;

import com.lanxi.couponcode.spi.abstractentity.AbstractMessage;
import com.lanxi.couponcode.spi.abstractentity.AbstractMessageObserver;

public interface MessageNotice {
	void addMessage(AbstractMessage message);
	void addMessages(Collection<AbstractMessage> messages);
	void notice(AbstractMessageObserver observer);
	void notice(Collection<AbstractMessageObserver> observers);
	void sendMessage(AbstractMessage message);
	void sendMessages(Collection<AbstractMessage> messages);
}
