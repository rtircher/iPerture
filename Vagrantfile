# -*- mode: ruby -*-

Vagrant.configure("2") do |config|
  config.vm.box = "centos-64-no-puppet-no-chef"
  # The url from where the 'config.vm.box' box will be fetched if it doesn't
  # already exist on the user's system.
  config.vm.box_url = "http://puppet-vagrant-boxes.puppetlabs.com/centos-64-x64-vbox4210-nocm.box"

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  config.vm.network :private_network, ip: "192.168.33.88"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  # config.vm.provider :virtualbox do |vb|
  #   # Don't boot with headless mode
  #   vb.gui = true
  #
  #   # Use VBoxManage to customize the VM. For example to change memory:
  #   vb.customize ["modifyvm", :id, "--memory", "1024"]
  # end

  # Provisioning
  config.vm.provision :shell, :path => "Vagrantprovision.sh"

  # Enable provisioning with chef solo, specifying a cookbooks path, roles
  # path, and data_bags path (all relative to this Vagrantfile), and adding
  # some recipes and/or roles.

  config.vm.provision :chef_solo do |chef|
    chef.cookbooks_path = ["chef/cookbooks", "chef/site-cookbooks"]
    chef.add_recipe "iPerture"
    # chef.roles_path = "chef/roles"
    # chef.data_bags_path = "chef/data_bags"
    # chef.add_role "web"

    # You may also specify custom JSON attributes:
    # chef.json = { :mysql_password => "foo" }
  end
end
