(ns app.hooks
  (:require
   [com.fulcrologic.fulcro.react.hooks :as fhooks]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.mutations :as m]))

(defn get-field-in
  [gotten-value field-keys]
  (get-in gotten-value field-keys))

(defn set-field-in
  [setter field-keys]
  (fhooks/use-callback
    (fn [new-value]
      (if (fn? new-value)
        (setter (fn [old-ancestor-value]
                  (update-in old-ancestor-value field-keys new-value)))
        (setter (fn [old-ancestor-value]
                  (assoc-in old-ancestor-value field-keys new-value)))))))

(defn use-field-in
  [gotten-value setter field-keys]
  [(get-field-in gotten-value field-keys) (set-field-in setter field-keys)])

(defn use-field
  [gotten-value setter field-key]
  (use-field-in gotten-value setter [field-key]))

(defn use-prop-setter
  [this prop-key]
  (fhooks/use-callback
    (fn [value]
      (if (fn? value)
        (m/set-value! this prop-key (value (prop-key (comp/props this))))
        (m/set-value! this prop-key value)))
    [this]))

(defn get-prop
  [this prop-key]
  ((comp/props this) prop-key))

(defn use-prop
  [this prop-key]
  [(get-prop this prop-key) (use-prop-setter this prop-key)])
