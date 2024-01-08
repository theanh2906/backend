#!/bin/bash

git status
sleep 3
git add -A
echo "Commit message: "
read msg
git commit -am $msg
git push
