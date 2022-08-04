(ns app.client-main
  (:require
   [app.application :refer [SPA]]
   [app.components.root :as root]
   [app.routing :as routing]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro-css.css-injection :as cssi]
   [com.fulcrologic.fulcro.algorithms.timbre-support :as ts]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.inspect.inspect-client :as inspect]
   [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
   [goog.net.Cookies :as Cookies]
   [goog.string :as gstring]
   [taoensso.timbre :as log]
   [app.components.material-ui :as mu]))

(defn ^:export refresh! []
  (log/info "Hot code Remount")
  (app/mount! SPA root/Root "app"))

(defn ^:export init! []
  (log/merge-config! {:min-level [["app.*" (if goog.DEBUG :debug :info)]
                                  ["*" :info]]
                      :output-fn ts/prefix-output-fn
                      :appenders {:console (ts/console-appender)}})
  (let [gcookies (Cookies/getInstance)]
    (-> gcookies (.set "csrf-token" (gstring/getRandomString))))
  (inspect/app-started! SPA)
  (app/set-root! SPA root/Root {:initialize-state? true})
  (dr/initialize! SPA)
  (app/mount! SPA root/Root "app" {:initialize-state? false})
  (routing/start!)
  (when-let [loader-el (js/document.getElementById "bootstrapping-loader")]
    (.remove loader-el))
  (root/init!))
