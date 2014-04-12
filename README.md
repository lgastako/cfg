# cfg

Simple configuration management for Clojure and/or ClojureScript applications.

## Usage

The configuration starts out as an empty clojure map protected by an atom.  You
can use `cfg.core/get` and `cfg.core/set` to manipulate the map.

```clojure
(ns your.app
  (:require [cfg.core :as cfg]))

(cfg/set :port 80)
;; config is now {:port 80}

(cfg/get :port)
;; -> 80
```
You can also replace the entire map all at once:

```clojure
(cfg/replace! {:port 8080 :debug true})
;; config is now {:port 8080 :debug false}
```

Or merge values into an existing map:

```clojure
(cfg/merge! {:debug true :database-url "sqlite://:memory:/"})
;; config is now {:port 8080 :debug true :database-url "sqlite://:memory:/"}
```

## License

Copyright © 2014 John Evans

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
