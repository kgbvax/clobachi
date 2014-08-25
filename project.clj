(defproject polly "0.1.0-SNAPSHOT"
  :description "Water my  jasminium plant triggered by a Koubachi plant sensor"
  :url "http://kgbvax.net/polly"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [clj-http "1.0.0"] ;http client
                 [cheshire "5.3.1"] ;json
                 [clojurewerkz/quartzite "1.3.0"] ;scheduling
                 ;[lein-kibit "0.0.8"] ;magic / recommend idiomatic clojure
                 [com.taoensso/timbre "3.2.1"] ;logging
                 [clj-time "0.8.0"] ;time handling
                 [com.ashafa/clutch "0.4.0-RC1"] ; couchdb
                 ;[org.clojure/clojurescript "0.0-2311"]
                 ]
  :repl-options { :init-ns polly.core
                  :init (init-repl) } ;start the repl in the primary namespace
  :aot :all
  :main ^:skip-aot polly.core
  :plugins [
            [jonase/eastwood "0.1.4"]
            [lein-ancient "0.5.5"]
            [lein-kibit "0.0.8"]
            ]
  :target-path "target/%s"
  :min-lein-version "2.0.0"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[clj-ns-browser "1.3.1"]]}})
