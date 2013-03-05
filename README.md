# iPerture

A website written in noir.

## Usage

All the following instructions assume they are run from the project root directory.

### In development

Use this handy command line that will (to be implemented with leiningen)

- start the web server
- watch changes to the haml and sass files
- watch for changes in the Clojure code under test and run the spec automatically
- watch for changes to the ClojureScript files and compile them automatically

### Starting all these task separately

Start webserver

```bash
lein ring server-headless
```

Use lein haml-sass plugin to compile the haml and sass files

```bash
lein haml-sass once
lein haml-sass auto
```

Run the Clojure unit tests automatically on file change

```bash
lein spec -a -f growl
```

The `-f growl` option is used to interact with growl notification system (it is not required to run the tests)

Generate javascripts from ClojureScripts

```bash
lein cljsbuild once # Builds the JavaScript files once
lein cljsbuild auto # Watches for changes and automatically builds the JavaScript files
```


## License

Copyright (C) 2013 Renaud Tircher
