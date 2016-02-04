(ns datomicschema
  (:use [datomic.api :only [q db] :as d]))

(def schema [{:db/id #db/id[:db.part/db]
              :db/ident :sag
              :db.install/_partition :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag/id
              :db/valueType :db.type/long
              :db/unique :db.unique/identity
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag/sagsbehandler
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag/oprettet
              :db/valueType :db.type/instant
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag/status
              :db/valueType :db.type/ref
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

              ;; :sag/status enum values
             [:db/add #db/id[:db.part/user] :db/ident :sag.status/ny]
             [:db/add #db/id[:db.part/user] :db/ident :sag.status/igang]
             [:db/add #db/id[:db.part/user] :db/ident :sag.status/lukket]

             {:db/id #db/id[:db.part/db]
              :db/ident :sag/type
              :db/valueType :db.type/ref
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

              ;; :sag/type enum values
             [:db/add #db/id[:db.part/user] :db/ident :sag.type/vurdering]
             [:db/add #db/id[:db.part/user] :db/ident :sag.type/klage]
             [:db/add #db/id[:db.part/user] :db/ident :sag.type/data]

             {:db/id #db/id[:db.part/db]
              :db/ident :sag/myndighed
              :db/valueType :db.type/ref
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

              ;; :sag/myndighed enum values
             [:db/add #db/id[:db.part/user] :db/ident :sag.myndighed/skat]
             [:db/add #db/id[:db.part/user] :db/ident :sag.myndighed/sanst]


             {:db/id #db/id[:db.part/db]
              :db/ident :sag/sagsakter
              :db/isComponent true
              :db/valueType :db.type/ref
              :db/cardinality :db.cardinality/many
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag.sagsakter/id
              :db/valueType :db.type/long
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag.sagsakter/tekst
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag.sagsakter/fil
              :db/isComponent true
              :db/valueType :db.type/ref
              :db/cardinality :db.cardinality/many
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag.sagsakter.fil/id
              :db/valueType :db.type/long
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag.sagsakter.fil/media-type
              :db/valueType :db.type/ref
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             ;; :sag.sagsakter.fil/media-type enum values
             [:db/add #db/id[:db.part/user] :db/ident :sag.sagsakter.fil.media-type/applicationword]
             [:db/add #db/id[:db.part/user] :db/ident :sag.sagsakter.fil.media-type/applicationpdf]
             [:db/add #db/id[:db.part/user] :db/ident :sag.sagsakter.fil.media-type/applicationpng]

             {:db/id #db/id[:db.part/db]
              :db/ident :sag.sagsakter.fil/fil-ref
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}



             {:db/id #db/id[:db.part/db]
              :db/ident :sag/opgaver
              :db/isComponent true
              :db/valueType :db.type/ref
              :db/cardinality :db.cardinality/many
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag.opgave/id
              :db/valueType :db.type/long
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             {:db/id #db/id[:db.part/db]
              :db/ident :sag.opgave/type
              :db/valueType :db.type/ref
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             ;; :sag.sagsakter.opgaver/type enum values
             [:db/add #db/id[:db.part/user] :db/ident :sag.opgave.type/opdater-data]
             [:db/add #db/id[:db.part/user] :db/ident :sag.opgave.type/manuel-vurdering]

             {:db/id #db/id[:db.part/db]
              :db/ident :sag.opgave/oprettet
              :db/valueType :db.type/instant
              :db/cardinality :db.cardinality/one
              :db.install/_attribute :db.part/db}

             ])


(defn create-schema [conn]
  @(d/transact conn schema))

(defn init-db [& [url1]]
  (let [url (if url1 url1 "datomic:free://localhost:4334/sag")]
    (d/create-database url)
    (let [conn (delay (d/connect url))]
      (create-schema @conn)
      @conn
      )))

(defn delete-db []
  (d/delete-database "datomic:dev://localhost:4334/billing"))
