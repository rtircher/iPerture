#! /bin/bash

DIR="$( cd "$( dirname "$0" )" && pwd )"

bundle --version &> /dev/null
if [ $? != 0 ]; then
  gem install bundler
fi
