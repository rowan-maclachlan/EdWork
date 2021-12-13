#!/bin/bash

URL=https://9m22uf5uj8.execute-api.us-east-1.amazonaws.com/prod/coffeeshops

rating=${1}

if [ $# -eq 3 ]; then
    lon=${2};
    lat=${3};
    curl -Gv -d "rating=${rating}" -d "lon=${lon}" -d "lat=${lat}" "${URL}";
else
    curl -G -d "rating=${rating}" "${URL}";
fi;


