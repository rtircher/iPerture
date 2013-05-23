include_recipe "java"
include_recipe "nginx"

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
