(ns donut.frontend.example.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [donut.frontend.example.app :as app]
            [donut.frontend.config :as dconf]
            [donut.frontend.core.flow :as dcf]
            [donut.frontend.core.utils :as dcu]
            [donut.frontend.sync.dispatch.echo :as dsde]
            [donut.system :as ds]
            [meta-merge.core :as meta-merge]))

(def fake-endpoint-routes
  "We're not making requests to real endpoints,"
  [["/user"      {:name     :users
                  :ent-type :user
                  :id-key   :id}]
   ["/user/{id}" {:name     :user
                  :ent-type :user
                  :id-key   :id}]])

(defn system-config
  "This is a function instead of a static value so that it will pick up
  reloaded changes"
  []
  (meta-merge/meta-merge
   dconf/default-config
   {::ds/defs {:donut.frontend {:sync-dispatch-fn dsde/sync-dispatch-fn
                                :sync-router      {:conf {:routes fake-endpoint-routes}}}}}))

(defn -main []
  ;; (rf/dispatch-sync [::stcf/init-system (system-config)])
  ;; (rf/dispatch [::bch/init])
  ;; (rf/dispatch-sync [::stnf/dispatch-current])
  (rf/dispatch-sync [::dcf/init-system (system-config)])
  (rdom/render [app/app] (dcu/el-by-id "app")))

(defonce initial-load (delay (-main)))
@initial-load

(defn stop [_]
  (rf/clear-subscription-cache!))
