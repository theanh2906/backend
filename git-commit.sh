#!/bin/bash

sleep 3
git add -A
echo "Commit message: "
read -r msg
git commit -am "$msg"
git push
