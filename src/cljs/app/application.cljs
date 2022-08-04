(ns app.application
  (:require
   [cognitect.transit :as t]
   [com.fulcrologic.fulcro.algorithms.transit :as ft]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.networking.file-upload :as fu]
   [com.fulcrologic.fulcro.networking.http-remote :as net]
   [edn-query-language.core :as eql]
   [goog.net.Cookies :as Cookies]
   [taoensso.timbre :as log]

   [app.components.material-ui :as mu]))

(defn wrap-file-upload
  "copied from com.fulcrologic.fulcro.networking.file-upload/wrap-file-upload"
  ([handler]
   (wrap-file-upload handler {}))
  ([handler transit-options]
   (fn [req]
     (if (fu/has-uploads? req)
       (try
         (let [ast (some-> req :body eql/query->ast)
               ast-to-send (update ast :children #(mapv (fn [n] (update n :params dissoc ::fu/uploads)) %))
               txn (eql/ast->query ast-to-send)
               form (js/FormData.)]
           (.append form "upload-transaction" (t/write (ft/writer transit-options) txn))
           (doseq [{:keys [dispatch-key params]} (:children ast)]
             (when-let [uploads (::fu/uploads params)]
               (doseq [{:file/keys [name content]} uploads]
                 (let [name-with-mutation (str dispatch-key "|" name)
                       js-value (-> content meta :js-value)
                       content (some-> js-value (fu/js-value->uploadable-object nil))]
                   (.append form "files" content name-with-mutation)))))
           (-> req
             (assoc :body form :method :post)
             (update :headers dissoc "Content-Type")
             (assoc :response-type :default)))
         (catch :default e
           (log/error e "Exception while converting mutation with file uploads.")
           {:body nil
            :method :post}))
       (handler req)))))

(defmutation prompt-global-error
  [{:keys [details] :as env}]
  (action [{:keys [state] :as env}]
    (swap! state assoc-in [:component/id :global-error] {:ui/active? true
                                                         :ui/details details})))

(defmutation close-global-error
  [{:keys [] :as env}]
  (action [{:keys [state] :as env}]
    (swap! state assoc-in [:component/id :global-error :ui/active?] false)))

(defn global-error-action
  [{:keys [app result] :as env}]
  (comp/transact! app [(prompt-global-error {:details (comp/fragment
                                                        (mu/dialog-title {} "Something went wrong :(")
                                                        (mu/dialog-content {}
                                                          (mu/dialog-content-text {}
                                                            "Our apologies. The server responded with an error. Refreshing the page may fix the problem.")
                                                          (when (:body result)
                                                            (comp/fragment
                                                              (mu/t {} "Here is what the server responded:")
                                                              (mu/text-field {:variant "outlined"
                                                                              :multiline true
                                                                              :fullWidth true
                                                                              :value (str (:body result))})))))})]))

(defn wrap-anti-csrf
  "Implement stateless anti-CSRF using the 'Custom Request Headers' and 'Double Submit Cookies' pattern.

  This cookie needs to be separate from the session id so that the session id cookie can be set as http-only (the
  default behavior by ring). "
  ([handler]
   (fn [request]
     (let [gcookies (Cookies/getInstance)
           csrf-token (-> gcookies (.get "csrf-token"))]
       (handler (update request :headers assoc "X-CSRF-Token" csrf-token))))))

(defonce SPA (app/fulcro-app
               {:remotes {;; :ws     (fws/fulcro-websocket-remote {})
                          :remote (net/fulcro-http-remote
                                    {:url "/_api"
                                     :request-middleware (-> identity
                                                           net/wrap-fulcro-request
                                                           wrap-file-upload
                                                           wrap-anti-csrf)
                                     :response-middleware (-> identity
                                                            net/wrap-fulcro-response
                                                                        ;; Below is a response intercepting logger for debugging
                                                                        ;; ((fn [handler] (fn [response]
                                                                        ;;                  (let [after-response (handler response)]
                                                                        ;;                    after-response))))
                                                            )})}
                :global-error-action global-error-action}))

(defn global-error!
  [{:keys [details]}]
  (comp/transact! SPA [(prompt-global-error {:details details})]))

(comment
  (-> SPA (::app/runtime-atom) deref ::app/indexes))
