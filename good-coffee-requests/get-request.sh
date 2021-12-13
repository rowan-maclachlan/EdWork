#!/bin/bash

URL=https://9m22uf5uj8.execute-api.us-east-1.amazonaws.com/prod/coffeeshops

if [ $# -ne 1 ]; then
    echo "Provide path parameter name"
    exit 1
fi

curl -G "${URL}/${1}" 

exit $!
