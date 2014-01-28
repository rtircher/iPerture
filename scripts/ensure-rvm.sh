#! /bin/bash

DIR="$( cd "$( dirname "$0" )" && pwd )"

# ensure rvm if installed
ruby_string=`cat .ruby-version`
gemset_name=`cat .ruby-gemset`

# Load or create the specified environment
if [[ -d "${rvm_path:-$HOME/.rvm}/environments" \
  && -s "${rvm_path:-$HOME/.rvm}/environments/${ruby_string}@${gemset_name}" ]] ; then
  \. "${rvm_path:-$HOME/.rvm}/environments/${ruby_string}@${gemset_name}"
else
  rvm --create  "${ruby_string}@${gemset_name}"
fi
