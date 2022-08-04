(ns app.routing
  (:require [app.application :refer [SPA]]
            [taoensso.timbre :as log]
            [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
            [pushy.core :as pushy]
            [clojure.string :as s]))

(declare route-to!)

(defn on-navigate
  [p]
  (case p
    (dr/change-route! SPA (-> p (s/split "/") rest vec))))

(defn process-token
  "When this function returns nil, on-navigate will not run, and clicking events will terminate early."
  [token]
  token)

(defonce history (pushy/pushy on-navigate process-token))

(defn start! []
  (pushy/start! history))

(defn route-to! [route-string]
  (pushy/set-token! history route-string))
