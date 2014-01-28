#! /bin/bash

DIR="$( cd "$( dirname "$0" )" && pwd )"

cat /etc/hosts | grep "192\.168\.33\.88\s*local\.iperture\.tircher\.com" &> /dev/null
if [ $? != 0 ]; then
  HOSTS_LINE="192.168.33.88   local.iperture.tircher.com local.iPerture.tircher.com"

  echo "Adding '$HOSTS_LINE' to your /etc/hosts file"
  sudo sh -c "echo \"$HOSTS_LINE\" >> /etc/hosts"
fi
