# iPerture

A website written in noir.

## Usage

All the following instructions assume they are run from the project root directory.

### Prerequisites 
Install GraphicsMagick. On MacOS:

```bash
brew install graphicsmagick  
```

Install Ruby, Bundler, and the rubygems:

```bash
gem install bundler
bundle install
```

### In development

Use this handy command line that will (to be implemented with leiningen)

- start the web server
- watch changes to the haml and sass files
- watch for changes in the Clojure code under test and run the spec automatically
- watch for changes to the ClojureScript files and compile them automatically

### Starting all these task separately

Use guard to compile the haml and sass files (it will also watch for changes to the files and recompile automatically)

```bash
bundle exec guard
```

You can also compile the assets once by running `bundle exec rake assets`

Generate javascripts from ClojureScripts

```bash
lein cljsbuild once # Builds the JavaScript files once
lein cljsbuild auto # Watches for changes and automatically builds the JavaScript files
```

Start webserver

```bash
lein ring server-headless
```

Run the Clojure unit tests automatically on file change

```bash
./scripts/tests
```

The `-f growl` option is used to interact with growl notification system (it is not required to run the tests)


## License

Copyright (C) 2013 Renaud Tircher
