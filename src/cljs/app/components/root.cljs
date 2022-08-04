(ns app.components.root
  (:require
   ["@monaco-editor/react" :default MonacoEditor]
   ["yaml" :as yaml]
   [app.components.material-ui :as mu]
   [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.react.hooks :as fhooks]
   [taoensso.timbre :as log]))

(def monaco-editor (interop/react-factory MonacoEditor))

(defn init!
  ;; This function runs immediately after the initialization of client/main.cljs
  [])

(defsc TitleTracker
  [_ _]
  {:ident (fn [] [:component/id :title-tracker])
   :query []
   :initial-state {}}
  (set! js/document.title "Form generator")
  nil)

(def ui-title-tracker (comp/factory TitleTracker))

(declare ui-form-item)

(defsc FormItem
  [_ {:keys [item form-value set-form-value]}]
  {}

  (let [output-key (get item "output_key")
        label (get item "label")
        default-value (get item "default_value")
        type (get item "type")
        form-field-value (get form-value output-key)
        set-form-field-value (fn [new-value]
                               (cond
                                 (fn? new-value)
                                 (set-form-value (fn [old-form-value]
                                                   (update old-form-value output-key new-value)))

                                 :else
                                 (set-form-value (fn [old-form-value]
                                                   (assoc old-form-value output-key new-value)))))]
    (mu/box {}
      (case type
        "single-choice"
        (let [choices (get item "choices")
              choices-by-value (->> choices
                                 (map (fn [choice] [(cond
                                                      (string? choice) choice
                                                      (map? choice) (get choice "value")
                                                      :else choice)
                                                    choice]))
                                 (into {}))
              chosen-choice (choices-by-value form-field-value)
              error-message (when (nil? chosen-choice)
                              (str "Please select a one of the given values"))]
          (comp/fragment
            (mu/text-field {:select true
                            :label label
                            :value form-field-value
                            :fullWidth true
                            :onChange (fn [e]
                                        (let [new-value (-> e .-target .-value)]
                                          (set-form-field-value new-value)))
                            :error (some? error-message)
                            :helperText error-message
                            :SelectProps {:displayEmpty true}
                            :InputLabelProps {:shrink true}}
              (->> choices
                (mapv (fn [choice]
                        (cond
                          (string? choice)
                          (mu/menu-item {:key choice
                                         :value choice}
                            choice)

                          (map? choice)
                          (let [{:strs [value label]} choice]
                            (mu/menu-item {:key value
                                           :value value}
                              (or label value)))

                          :else (mu/alert {:severity "error"}
                                  (mu/alert-title {} (str "Not sure how to parse choice"))
                                  (str "choice = " choice)))))))
            (let [{:strs [subquestions]} chosen-choice]
              (when (seq subquestions)
                (mu/stack {:gap "32px"
                           :paddingTop "32px"
                           :paddingLeft "32px"}
                  (->> subquestions
                    (mapv (fn [item]
                            (let [output-key (get item "output_key")]
                              (ui-form-item {:key output-key
                                             :item item
                                             :form-value form-value
                                             :set-form-value set-form-value}))))))))))

        "multiple-choice"
        (let [choices (get item "choices")
              required? (or (get item "required") false)
              choices-by-value (->> choices
                                 (map (fn [choice] [(cond
                                                      (string? choice) choice
                                                      (map? choice) (get choice "value")
                                                      :else choice)
                                                    choice]))
                                 (into {}))
              chosen-choices (map choices-by-value form-field-value)
              error-message (when (and required? (empty? chosen-choices))
                              (str "At least one selection is required."))]
          (comp/fragment
            (mu/form-control {}
              (mu/form-label {} label)
              (mu/form-group {}
                (->> choices
                  (mapv (fn [choice]
                          (let [choice* (cond
                                          (map? choice) choice
                                          (string? choice) {"value" choice
                                                            "label" choice}
                                          :else {"error" {:title (str "Not sure how to parse choice")
                                                          :message (str "choice = " choice)}})

                                error (get choice* "error")
                                value (get choice* "value")
                                label (get choice* "label")]
                            (if (some? error)
                              (let [{:keys [title message]} error]
                                (mu/alert {:key value
                                           :severity "error"}
                                  (mu/alert-title {} title)
                                  message))
                              (mu/form-control-label {:key value
                                                      :label label
                                                      :control (mu/checkbox {:checked (contains? (set chosen-choices) choice*)
                                                                             :onChange (fn [e]
                                                                                         (let [new-checked (-> e .-target .-checked)]
                                                                                           (if new-checked
                                                                                             (set-form-field-value (fn [old-form-field-value]
                                                                                                                     (-> old-form-field-value
                                                                                                                       set (conj value) vec)))
                                                                                             (set-form-field-value (fn [old-form-field-value]
                                                                                                                     (-> old-form-field-value
                                                                                                                       set (disj value) vec))))))})}))))))))

            (->> chosen-choices
              (mapv (fn [chosen-choice]
                      (let [{:strs [subquestions]} chosen-choice]
                        (when (seq subquestions)
                          (mu/stack {:gap "32px"
                                     :paddingTop "32px"
                                     :paddingLeft "32px"}
                            (->> subquestions
                              (mapv (fn [item]
                                      (let [output-key (get item "output_key")]
                                        (ui-form-item {:key output-key
                                                       :item item
                                                       :form-value form-value
                                                       :set-form-value set-form-value})))))))))))))

        "single-line-string"
        (let [regex (get item "regex")
              error-message (when (and regex (not (-> regex (js/RegExp.) (.test form-field-value))))
                              (str "The value does not satisfy the regular expression \"" regex "\". Example value: \"" default-value "\"."))]
          (mu/text-field {:type "input"
                          :label label
                          :fullWidth true
                          :value form-field-value
                          :InputLabelProps {:shrink true}
                          :error (some? error-message)
                          :helperText error-message
                          :onChange (fn [e]
                                      (let [new-value (-> e .-target .-value)]
                                        (set-form-field-value new-value)))
                          :multiple false}))

        "multi-line-string"
        (let [regex (get item "regex")
              error-message (when (and regex (not (-> regex (js/RegExp.) (.test form-field-value))))
                              (str "The value does not satisfy the regular expression \"" regex "\". Example value: \"" default-value "\"."))]
          (mu/text-field {:type "input"
                          :label label
                          :fullWidth true
                          :multiline true
                          :rows 4
                          :value form-field-value
                          :InputLabelProps {:shrink true}
                          :error (some? error-message)
                          :helperText error-message
                          :onChange (fn [e]
                                      (let [new-value (-> e .-target .-value)]
                                        (set-form-field-value new-value)))
                          :multiple false}))

                              ;; else
        (mu/alert {:severity "error"}
          (mu/alert-title {} (str "Unknown type \"" type "\""))
          (str "item = " item))))))

(def ui-form-item (comp/factory FormItem {:keyfn :key}))

(defsc FormPage
  [this {:ui/keys [] :as props}]
  {:ident (fn [] [:component/id :form])
   :query []
   :initial-state {}
   :route-segment ["form"]
   :use-hooks? true}
  (let [[questionnaire-blueprint set-questionnaire-blueprint] (fhooks/use-state nil)
        [form-value set-form-value] (fhooks/use-state nil)
        monaco-editor-ref (fhooks/use-ref nil)
        load-json (fhooks/use-callback
                    (fn [])
                    [])]
    (mu/stack {:direction "row"
               :gap "16px"}
      (mu/stack {:padding "16px"
                 :gap "16px"}
        (mu/stack {:direction "row"
                   :flex "none"
                   :gap "8px"}
          (mu/button {:onClick (fn []
                                 (-> (js/fetch "example.yaml")
                                   (.then (fn [res] (.text res)))
                                   (.then (fn [res-text]
                                            (when-let [editor (.-current monaco-editor-ref)]
                                              (-> editor (.setValue res-text)))))))}
            "Load example")
          (mu/stack {})
          (mu/button {:variant "contained"
                      :onClick (fn []
                                 (when-let [editor (-> monaco-editor-ref .-current)]
                                   (let [yaml-text (-> editor (.getValue))
                                         parsed-json (try (yaml/parse yaml-text)
                                                          (catch js/Error e
                                                            (log/spy e)
                                                            {:parser-error e}))
                                         questionnaire-blueprint (js->clj parsed-json)]
                                     (set-questionnaire-blueprint questionnaire-blueprint)
                                     (when (map? questionnaire-blueprint)
                                       (let [questions (get questionnaire-blueprint "questions")]
                                         (set-form-value (->> questions
                                                           (map (fn [item]
                                                                  (let [output-key (get item "output_key")
                                                                        label (get item "label")
                                                                        default-value (get item "default_value")
                                                                        type (get item "type")]
                                                                    [output-key default-value])))
                                                           (into {}))))))))}
            "Parse"))
        (monaco-editor {:height "100%"
                        :defaultLanguage "yaml"
                        :defaultValue "# Your form definition (in YAML) here"
                        :onMount (fn [editor monaco]
                                   (set! (.-current monaco-editor-ref) editor))}))
      (mu/stack {:padding "16px"
                 :gap "16px"}
        (mu/stack {:direction "row"
                   :flex "none"
                   :height "33px"
                   :gap "8px"}
          (mu/stack {:typography "h5"} "Form"))
        (mu/stack {:overflow "auto"}
          (if (nil? questionnaire-blueprint)
            (mu/box {} "No blueprint loaded.")
            (let [title (get questionnaire-blueprint "title")
                  questions (get questionnaire-blueprint "questions")]
              (mu/stack {:gap "32px"
                         :paddingTop "16px"
                         :overflow "auto"}
                (mu/box {:typography "h5"} title)
                (->> questions
                  (mapv (fn [item]
                          (let [output-key (get item "output_key")]
                            (ui-form-item {:key output-key
                                           :item item
                                           :form-value form-value
                                           :set-form-value set-form-value}))))))))))
      (mu/stack {:padding "16px"
                 :gap "16px"}
        (mu/stack {:direction "row"
                   :flex "none"
                   :height "33px"
                   :gap "8px"}
          (mu/stack {:typography "h5"} "Form value"))
        (mu/stack {:overflow "auto"}
          (mu/box {:component "pre"} (str (js/JSON.stringify (clj->js form-value) nil 2))))))))

(def ui-form-page (comp/factory FormPage))

(defsc TopChrome
  [_ {:ui/keys [form-page title-tracker]}]
  {:ident (fn [] [:component/id :top-chrome])
   :query [{:ui/title-tracker (comp/get-query TitleTracker)}
           {:ui/form-page (comp/get-query FormPage)}]
   :initial-state {:ui/form-page {}
                   :ui/title-tracker {}}}
  (mu/stack {}
    (mu/theme-provider
      {:theme mu/light-theme}
      (mu/scoped-css-baseline
        {:sx {:height "100vh"
              :minHeight "768px"
              :minWidth "1800px"
              :position "relative"
              :display "flex"}}
        (ui-form-page form-page)))
    (ui-title-tracker title-tracker)))

(def ui-top-chrome (comp/factory TopChrome))

(defsc Root [this {:root/keys [top-chrome]}]
  {:query [{:root/top-chrome (comp/get-query TopChrome)}]
   :initial-state {:root/top-chrome {}}}
  (ui-top-chrome top-chrome))
