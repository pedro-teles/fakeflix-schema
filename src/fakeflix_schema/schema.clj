(ns fakeflix-schema.schema
  (:require [schema.core :as s]))

(def FloatNum
  "A float"
  (schema.core/pred float? 'float?))

(s/defn keyword-skeleton->keyword-schema
  [skeleton :- [s/Any]]
  (let [keyword (first skeleton)
        attributes (second skeleton)
        required? (:required attributes)
        schema (:schema attributes)]
    (if required?
      {keyword schema}
      {(s/optional-key keyword) schema})))

(s/defn skeleton->schema :- {s/Any s/Any}
  [skeleton :- {s/Keyword {s/Keyword s/Any}}]
  (->> skeleton
       seq
       (map keyword-skeleton->keyword-schema)
       (reduce merge)))

(s/defn strict-schema :- {s/Any s/Any}
  [skeleton :- {s/Keyword {s/Keyword s/Any}}]
  (skeleton->schema skeleton))

(s/defn loose-schema :- {s/Any s/Any}
  [skeleton :- {s/Keyword {s/Keyword s/Any}}]
  (-> skeleton
      skeleton->schema
      (assoc s/Any s/Any)))
