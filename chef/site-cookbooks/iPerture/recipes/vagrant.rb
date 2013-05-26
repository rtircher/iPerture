include_recipe "iPerture"
include_recipe "leiningen"

LOG_FOLDER = "/vagrant/log"

directory LOG_FOLDER do
  mode "0644"
  action :create
end

execute "lein-ring-server" do
  cwd "/vagrant"
  command <<-EOS
kill `ps ax | grep [c]ompojure | awk '{print $1}'`
for i in {0..30}; do
  netstat -an | grep 5000 &> /dev/null
  if [ $? == 0 ]; then
    sleep 1
  else
    break
  fi
done
LEIN_ROOT=1 lein ring server-headless &> #{LOG_FOLDER}/development.log &
EOS
end
