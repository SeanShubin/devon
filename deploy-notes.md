### gpg
- generate the key, note the key id
    - gpg2 --gen-key
- use the key id to send the key to a key server
    - gpg2 --keyserver hkp://pgp.mit.edu --send-keys B45DA2B9

### maven
- generate the signatures and deploy to staging
    - mvn clean
    - mvn deploy -Dgpg.passphrase=*** -Dgpg.keyname=B45DA2B9
- check the signatures
    - https://oss.sonatype.org/#stagingRepositories
    - Staging Repositories
    - Close
    - Wait a few seconds
    - Refresh

### settings.xml

    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
        <servers>
            <server>
                <id>maven-staging</id>
                <username>SeanShubin</username>
                <password>***</password>
            </server>
        </servers>
        <mirrors>
            <mirror>
                <id>maven-staging</id>
                <url>https://oss.sonatype.org/service/local/staging/deploy/maven2</url>
                <mirrorOf>maven-staging</mirrorOf>
            </mirror>
        </mirrors>
    </settings>
