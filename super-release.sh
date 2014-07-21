#!/bin/bash -ex

DIR=$(dirname $0)
CLOSE_VERSION="$1"
SNAPSHOT_VERSION="$2"
LOCAL_REPOSITORY=$3

STOP=0

if [ $CLOSE_VERSION == '' ]]; then 
        STOP=1;
fi;

if [[ $SNAPSHOT_VERSION == '' ]]; then 
        STOP=1;
fi;

if git status -s | grep ".\\+" ; then 
        STOP=1; 
fi

if [ $STOP -eq 0 ]; then
        mvn clean install
fi

if  git status -s | grep ".\\+" ; then 
        STOP=1; 
fi

if [ $STOP -eq 0 ]; then
        mvn versions:set -DnewVersion=$CLOSE_VERSION
        git commit -am "releasing jpa-query-paginator-$CLOSE_VERSION"
        git tag jpa-query-paginator-$CLOSE_VERSION
        git push origin jpa-query-paginator-$CLOSE_VERSION

        mvn clean install -Dmaven.test.skip.exec

        mvn deploy -e -Dmaven.test.skip.exec -Dlocal.repository=$LOCAL_REPOSITORY
 
        mvn versions:set -DnewVersion=$SNAPSHOT_VERSION -DgenerateBackupPoms=false
        git commit -am "setting next version to releasing jpa-query-paginator-$CLOSE_VERSION"
        git push


        mvn versions:commit
else
    echo "Script usage: $(basename $0) 0.0.1 0.0.2-SNAPSHOT localrepository"
fi


