#!/bin/bash

URL=https://9m22uf5uj8.execute-api.us-east-1.amazonaws.com/prod/coffeeshops

curl -vX POST ${URL} \
    -d @post-template.json \
    --header "Content-Type: application/json"
