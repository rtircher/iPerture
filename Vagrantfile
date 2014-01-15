# -*- mode: ruby -*-

Vagrant.configure("2") do |config|
  config.vm.box = "iPerture"
  config.vm.box_url = "http://puppet-vagrant-boxes.puppetlabs.com/centos-64-x64-vbox4210-nocm.box"

  config.vm.network :private_network, ip: "192.168.33.88"

  # Provider-specific configuration
  config.vm.provider :virtualbox do |vb|
    vb.customize ["modifyvm", :id, "--memory", "1024"]
    vb.customize ["modifyvm", :id, "--cpus", "2"]
  end

  # Provisioning
  config.vm.provision :shell, :path => "Vagrantprovision.sh"

  config.vm.provision :chef_solo do |chef|
    chef.cookbooks_path = ["chef/cookbooks", "chef/site-cookbooks"]
    chef.add_recipe "iPerture::vagrant"
    # chef.roles_path = "chef/roles"
    # chef.data_bags_path = "chef/data_bags"
    # chef.add_role "web"

    # You may also specify custom JSON attributes:
    # chef.json = { :mysql_password => "foo" }
  end
end
