(defproject wacnet "2.1.3-alpha"
  :description "Webserver to browse a BACnet network"
  :url "https://hvac.io"
  :license {:name "GNU General Public License V3"
            :url "http://www.gnu.org/licenses/gpl-3.0.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]

                 ;; BACnet
                 [bacure "1.0.8"]

                 [io.hvac.vigilia/vigilia-logger "1.0.10"]

                 ;; webserver stuff
                 [bidi "2.0.16"]      ; routing
                 [aleph "0.4.3"] ; server
                 [aleph-middleware "0.1.1"]
                 [yada "1.2.2" :exclusions [manifold]];[yada "1.1.29"]
                 
                 [trptcolin/versioneer "0.2.0"]

                 ;; error logs
                 [org.slf4j/slf4j-nop "1.7.25"]
                 

                 ;; nREPL
                 [org.clojure/tools.nrepl "0.2.12"]
                 [cider/cider-nrepl "0.11.0"]

                 ;; cljs
                 [org.clojure/clojurescript "1.9.89"]
                 [org.clojure/core.async "0.2.385"]
                 
                 [reagent "0.6.0"]

                 [org.clojars.frozenlock/reagent-modals "0.2.6"
                  :exclusions [reagent]]
                 [cljs-ajax "0.3.9"]
                 [re-com "1.3.0" :exclusions [reagent]]
                 [cljsjs/fixed-data-table "0.6.3-0"
                  :exclusions [cljsjs/react]]]

  :plugins [[lein-environ "1.0.1"]
            [lein-cljsbuild "1.1.1"]            
            [lein-externs "0.1.5"]] 

  :manifest {"SplashScreen-Image" "public/img/splash.png"}

  :min-lein-version "2.5.0"

  :main wacnet.server

  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]


  :cljsbuild {:builds {:app {:source-paths ["src/cljs" "src/cljc"]
                             :compiler {:output-to "target/cljsbuild/public/js/app.js"
                                        :output-dir "target/cljsbuild/public/js/out"
                                        :asset-path   "js/out"
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :resource-paths ["resources" "target/cljsbuild"]

  :profiles {:dev {;:repl-options {:init-ns wacnet.repl}

                   :dependencies [[ring/ring-mock "0.3.0"]
                                  [ring/ring-devel "1.4.0"]
                                  [prone "0.8.3"]
                                  [lein-figwheel "0.5.0-6"
                                   :exclusions [org.clojure/core.memoize
                                                ring/ring-core
                                                org.clojure/clojure
                                                org.ow2.asm/asm-all
                                                org.clojure/data.priority-map
                                                org.clojure/tools.reader
                                                org.clojure/clojurescript
                                                org.clojure/core.async
                                                org.clojure/tools.analyzer.jvm]]
                                  ;; [org.clojure/clojurescript "1.7.170"
                                  ;;  :exclusions [org.clojure/clojure org.clojure/tools.reader]]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [devcards "0.2.1-7"
                                   :exclusions [org.clojure/tools.reader]]
                                  [pjstadig/humane-test-output "0.7.1"]
                                  ]

                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.5.4-5"
                              :exclusions [org.clojure/core.memoize
                                           ring/ring-core
                                           org.clojure/clojure
                                           org.ow2.asm/asm-all
                                           org.clojure/data.priority-map
                                           org.clojure/tools.reader
                                           org.clojure/clojurescript
                                           org.clojure/core.async
                                           org.clojure/tools.analyzer.jvm]]
                             ;[org.clojure/clojurescript "1.7.170"]
                             ;[cider/cider-nrepl "0.11.0"]
                             [org.clojure/tools.namespace "0.3.0-alpha2"
                              :exclusions [org.clojure/tools.reader]]
                             [refactor-nrepl "2.0.0"
                              :exclusions [org.clojure/clojure]]
                             ]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :nrepl-port 7002
                              :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"
                                                 "cider.nrepl/cider-middleware"
                                                 "refactor-nrepl.middleware/wrap-refactor"
                                                 ]
                              :css-dirs ["resources/public/css"]
                              :ring-handler wacnet.handler/app}

                   :env {:dev true}

                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]
                                              :compiler {:main "wacnet.dev"
                                                         :source-map true}}
                                        }

                               }}

             :uberjar {:source-paths ["env/prod/clj"]
                       :prep-tasks ["compile" ["cljsbuild" "once"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true
                       :cljsbuild {:jar true
                                   :builds {:app
                                            {:source-paths ["env/prod/cljs"]
                                             :compiler
                                             {:optimizations :advanced
                                              ;; :source-map "target/cljsbuild/public/js/app.js.map"
                                              ;; :source-map-path "js/out"
                                              :externs ^:replace ["externs.js"]
                                              :pretty-print false}
                                             }}}}}
  
  :source-paths ["src/clj" "src/cljs" "src/cljc"])
