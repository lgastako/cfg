(ns cfg.core
   (:refer-clojure :exclude [get set]))

(def ^:private values (atom {}))

(defn ensure-ks
  "Given a korks, ensures that it's a ks."
  [korks]
  (if (sequential? korks) korks [korks]))

(defn dissoc-in
  "Dissoc `korks` (as opposed to dissoc's single `k`) from map `m`."
  ([m] (dissoc-in m nil))
  ([m korks]
     (let [ks (ensure-ks korks)]
       (condp = (count ks)
         0 m
         1 (dissoc m (first ks))
         (if (get-in m ks)
           (update-in m (butlast ks) dissoc (last ks))
           (recur m (butlast ks)))))))

(defn get
  "Lookup a config key or keys with an optional default, e.g:

   (cfg/get :foo)                 ;; => (:foo values nil)
   (cfg/get :foo \"bar\")         ;; => (:foo values \"bar\")
   (cfg/get [:foo :bar])          ;; => (:bar (:foo values) nil)
   (cfg/get [:foo :bar] \"baz\")  ;; => (:bar (:foo values) \"baz\")
                                  ;; etc
  "
  ([korks]
     (get korks nil))
  ([korks default]
     (get-in @values (ensure-ks korks) default)))

(defn set
  "Set the value for a key or keys, eg.:

   (cfg/set :foo :bar)       ;; Sets the value at :foo to :bar
   (cfg/set [:a :b :c] :baz) ;; Sets the value at path [:a :b :c] to :d
                             ;; Creating empty maps for any missing keys
  "
  ([korks val]
     (swap! values assoc-in (ensure-ks korks) val)))

(defn del
  "Clear the value of a key or keys, eg.:

   (cfg/del :foo)
   (cfg/del [:a :b :c]
  "
  [& korkses]

  (swap! values
         (fn [values]
           (reduce #(dissoc-in %1 %2) values korkses))))

(defn replace!
  "Replace the entire configuration with a new configuration map.
   If more than one map is supplied they will be merged."
  [& ms]
  (reset! values (apply merge ms)))

(defn merge!
  "Merge the new values into the existing configuration."
  [& ms]
  (swap! values merge ms))
