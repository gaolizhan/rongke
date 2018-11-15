#!/bin/sh
git pull

mvn install -Ppro

docker build -t registry-vpc.cn-shanghai.aliyuncs.com/naquyong/yironghua:prod .

docker push registry-vpc.cn-shanghai.aliyuncs.com/naquyong/yironghua:prod

