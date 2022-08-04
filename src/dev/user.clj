(ns user
  (:require
   [clojure.spec.alpha :as s]
   [clojure.tools.namespace.repl :as tools-ns]
   [expound.alpha :as expound]
   [mount.core :as mount]
   [shadow.cljs.build-report :as sbr]
   [shadow.cljs.devtools.api :as shadow]))

;; ==================== SERVER ====================
(tools-ns/set-refresh-dirs "src/dev" "src/clj" "src/cljs" "src/cljc")
;; Change the default output of spec to be more readable
(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start     [] (mount/start-with-args {:debug? false}))
(defn start-dev [] (mount/start-with-args {:debug? true}))
(defn stop      [] (mount/stop))

(defn restart     [] (stop) (tools-ns/refresh :after 'user/start))
(defn restart-dev [] (stop) (tools-ns/refresh :after 'user/start-dev))

(defn watch       [] (shadow/watch       :app))
(defn stop-worker [] (shadow/stop-worker :app))
(defn compile-dev [] (shadow/compile     :app))
(defn release     [] (shadow/release     :app))

(defn build-report [] (sbr/-main :app (.getPath (java.io.File/createTempFile "build-report-" ".html"))))
