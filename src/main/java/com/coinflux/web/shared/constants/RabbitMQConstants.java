package com.coinflux.web.shared.constants;


public class RabbitMQConstants {
    // === Exchange ===
    public static final String COIN_EXCHANGE_RATES_QUEUE = "coin.exchange.rates.queue";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String EMAIL_EXCHANGE = "email.exchange";


    // === Queues ===
    public static final String COIN_EXCHANGE_RATES_EXCHANGE = "coin.exchange.rates.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String EMAIL_QUEUE = "email.queue";


    // === Routing Keys ===
    public static final String COIN_EXCHANGE_RATES_ROUTING_KEY = "coin.exchange.rates.routingKey";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.routingKey";
    public static final String EMAIL_ROUTING_KEY = "email.routingKey";
}
