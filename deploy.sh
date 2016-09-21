#!/usr/bin/env bash

mvn clean
mvn deploy -P stage --settings=deploy-to-maven-central-settings.xml -Dgpg.keyname=B45DA2B9 -Dgpg.passphrase=***
