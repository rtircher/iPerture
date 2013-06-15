include_recipe "iPerture"
include_recipe "leiningen"

LOG_FOLDER = "/vagrant/log"

directory LOG_FOLDER do
  mode "0644"
  action :create
end


service 'iptables' do
  action :stop
end

execute "chkconfig-iptables-off" do
  command "chkconfig iptables off"
end

execute "lein-deps" do
  cwd "/vagrant"
  command "lein deps"
end

execute "kill-ring-server" do
  command <<-EOS
kill `ps ax | grep "[r]ing server-headless" | awk '{print $1}'`
for i in {0..30}; do
  netstat -an | grep 5000 &> /dev/null
  if [ $? == 0 ]; then
    sleep 1
  else
    break
  fi
done
EOS
end

execute "lein-ring-server" do
  cwd "/vagrant"
  command "LEIN_ROOT=1 lein ring server-headless &> #{LOG_FOLDER}/development.log &"
end
