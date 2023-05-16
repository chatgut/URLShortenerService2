URL shortener service


This microservice application offers the following features:


Create short link for url with expired time 24 hour.

To start application run docker-compose.yml and you will make automatic app and mysql database.



---
Endpoints

POST /generate - Create a new short link

GET /{shortlink}


PUT /generated - put url to get short link
Example:

{
"url":"https://www.google.com/search?q=funny+bild&sxsrf=APwXEddii8641FDUtoS1ZqU0lSlPuKykZQ:1683457482701&source=lnms&tbm=isch&sa=X&ved=2ahUKEwiHr4Dqh-P-AhUhVfEDHR3GCT4Q_AUoAXoECAEQAw&biw=1920&bih=975&dpr=1#imgrc=h11oHZqoZQwR5M"
}

------

Example:
{
"originalUrl": "https://www.google.com/search?q=funny+bild&sxsrf=APwXEddii8641FDUtoS1ZqU0lSlPuKykZQ:1683457482701&source=lnms&tbm=isch&sa=X&ved=2ahUKEwiHr4Dqh-P-AhUhVfEDHR3GCT4Q_AUoAXoECAEQAw&biw=1920&bih=975&dpr=1#imgrc=h11oHZqoZQwR5M",
"shortLink": "cf157df3",
"expirationDate": "2023-05-17T08:25:03.265024448"
}

-----

GET / {short link}

Example:
http://localhost:8004/cf157df3

