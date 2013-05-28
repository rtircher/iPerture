guard 'haml', :output => 'resources/public/html',
              :input => 'src/iPerture/views/haml' do
  watch %r{^src/.+(\.haml)}
end

guard 'sass', :output => 'resources/public/css',
              :input => 'src/iPerture/views/sass',
              :smart_partials => true,
              :style => :nested,
              :line_numbers => true
