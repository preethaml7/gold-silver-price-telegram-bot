# gold-silver-price-telegram-bot

SpringBoot application that scrapes gold and silver price from a website and sends a telegram message at the scheduled
time.

Instructions on how to run the application are below:

1) Add telegram token and chat-id in the application.yml file

```   
telegram-bot:
   token: telegramToken
   message-receiver:
      chat-id: chatID
```

2) Change the scheduled time as required in the main Application class (currently it is configured to run at 8 AM Indian
   Time)

-> To build the docker image run the following command:
docker build -t gold-silver-price-telegram-bot:v1.0.0 .

Docker Stack:

```
version: '2'
services:
  gold-silver-price-telegram-bot:
    container_name: gold-silver-price-telegram-bot
    image: 'gold-silver-price-telegram-bot:v1.0.0'
    restart: unless-stopped
```

Docker run command:
docker run -dit --name gold-silver-price-telegram-bot gold-silver-price-telegram-bot:v1.0.0

Telegram Message:
![Telegram Message](sample-telegram-msg.png)


