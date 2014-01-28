#! /bin/bash

DIR="$( cd "$( dirname "$0" )" && pwd )"

VAGRANT_VERSION=`vagrant --version | awk '{print $(NF)}'`
# Test Vagrant version is greater than 1.2.2
echo $VAGRANT_VERSION | grep -q "\(1\.2\.[2-9]\)\|\(1\.[3-9]\.\d\)\|\([2-9]\.\d\.\d\)"

if [[ $? != 0 ]]; then
  echo "Please install latest vagrant version from http://www.vagrantup.com/downloads.html"
  exit 1
fi
