include_recipe "java"

group "www-data" do
  append true
end
user "www-data" do
  gid "www-data"
end
include_recipe "nginx"

cookbook_file "/etc/nginx/nginx.conf" do
  source "nginx.conf"
  mode 0644
  owner "root"
  group "root"
  notifies :reload, 'service[nginx]'
end

cookbook_file "/etc/nginx/sites-available/iPerture.tircher.com.conf" do
  source "iPerture.tircher.com.conf"
  mode 0644
  owner "root"
  group "root"
  notifies :reload, 'service[nginx]'
end

link "/etc/nginx/sites-enabled/iPerture.tircher.com.conf" do
  to "/etc/nginx/sites-available/iPerture.tircher.com.conf"
end

execute "open-port-80" do
  command 'iptables -A INPUT -m state --state NEW -m tcp -p tcp --dport 80 -j ACCEPT'
  user 'root'
  not_if "iptables --line-numbers -n -L | grep 'ACCEPT.*80'"
end

execute "save-iptables" do
  command "service iptables save"
end

service 'iptables' do
  action :restart
end
