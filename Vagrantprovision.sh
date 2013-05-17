#! /bin/bash

# ensure chef is installed
chef-solo -v &> /dev/null
if [ $? != 0 ]; then
  curl -L https://www.opscode.com/chef/install.sh | bash
fi
