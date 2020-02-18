FROM 241211947956.dkr.ecr.us-east-1.amazonaws.com/bc-java:master2

WORKDIR /workspace

ENV APP_ENVIRONMENT prod

ENV APP_NAME bc-meli-core-catalog
ENV APP_PORT 7000
ENV JAVA_OPTS="-Xmx512M -Xms512M"

ENV DB_URL=jdbc:postgresql://<HOST>:5432/<DATA_BASE>
ENV DB_USER=postgres
ENV DB_PASS=postgres

ENV MELI_CATALOG_LISTING_ELIGIBILITY_URL=https://api.mercadolibre.com/items/{itemId}/catalog_listing_eligibility
ENV MELI_API_ITEMS_URL=https://api.mercadolibre.com/items
ENV MELI_API_USERS_ITEMS_URL=https://api.mercadolibre.com/users/{meli_id}/items/search
ENV MELI_API_OAUTH_TOKEN = https://api.mercadolibre.com/oauth/token

ENV REDIS_HOST=redis
ENV REDIS_PORT=6379

ENV DD_SERVICE_NAME  bc-meli-core-catalog

COPY ./target/*.jar /workspace/app.jar