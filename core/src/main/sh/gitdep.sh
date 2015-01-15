#!/bin/bash

cd ${0%/*}

java -jar gitdep.jar "$@"
