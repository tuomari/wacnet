(ns wacnet.local-device
  (:require [bacure.core]
            [bacure.local-device :as ld]
            [wacnet.vigilia.logger.timed :as timed]
            [trptcolin.versioneer.core :as version]
            [wacnet.nrepl]))

(defn initialize
  "Initialize the local BACnet device." []
  ;(when-not (logger/maybe-start-logging) ;; start logging
  (bacure.core/boot-up
   {:vendor-name "HVAC.IO"
    :vendor-identifier 697
    :model-name "Wacnet"
    :object-name "Wacnet webserver"
    :application-software-version (version/get-version "wacnet" "wacnet")
    :description 
    (str "Wacnet: a BACnet webserver, but also a portable BACnet toolkit! \n"
         "Access the web interface at \n"
         "http://"(bacure.network/get-any-ip)":47800, "
         "or use the Clojure REPL on port " (:port @wacnet.nrepl/server)".")})
  (timed/maybe-start-logging))
