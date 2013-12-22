require 'guard'

namespace :assets do
  desc 'Generate HTML from all source'
  task :html do
    Guard.setup
    Guard.guards('haml').first.run_all
  end

  desc 'Generate CSS from all source'
  task :css do
    Guard.setup
    Guard.guards('sass').first.run_all
  end
end

descs 'Generate all assets'
task :assets => ["assets:html", "assets:css"]
