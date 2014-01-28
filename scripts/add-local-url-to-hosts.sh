#! /bin/bash

DIR="$( cd "$( dirname "$0" )" && pwd )"

cat /etc/hosts | grep "192\.168\.33\.88\s*local\.iperture\.tircher\.com" &> /dev/null
if [ $? != 0 ]; then
  echo "Adding '192.168.33.88   local.iperture.tircher.com local.iPerture.tircher.com' to your /etc/hosts file"
  sudo sh -c 'echo "192.168.33.88\tlocal.iperture.tircher.com local.iPerture.tircher.com" >> /etc/hosts'
fi
