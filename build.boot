(set-env!
  :project 'webapp-skeleton
  :version "0.1.0"
  :dependencies
  '[[hoplon/boot-hoplon "0.1.10"]
    [hoplon "6.0.0-alpha10"]
    [adzerk/boot-reload "0.3.2" :scope "test"]
    [pandeiro/boot-http "0.6.3"]
    [org.clojure/clojurescript "1.7.122"]
    [adzerk/boot-cljs "1.7.48-4"]
    [cljsjs/boot-cljsjs "0.5.0" :scope "test"]
    [exicon/semantic-ui "2.0.6-SNAPSHOT"]]
  :source-paths #{"src"}
  :resource-paths #{"assets"})

(require
  '[pandeiro.boot-http :refer [serve]]
  '[cljsjs.boot-cljsjs :refer [from-cljsjs]]
  '[hoplon.boot-hoplon :refer [hoplon prerender html2cljs]]
  '[adzerk.boot-reload :refer [reload]]
  '[adzerk.boot-cljs :refer [cljs]])

(task-options!
  serve {:port 8000}
  speak {:theme "woodblock"}
  cljs {:compiler-options {:pseudo-names false}})

(deftask dev []
  (comp
    (serve)
    (from-cljsjs :profile :development)
    (sift :move {#"^semantic-ui.inc.css$" "semantic-ui.css"})
    (watch)
    (hoplon :pretty-print true :refers '#{hoplon.ext})
    (reload)
    (cljs :optimizations :none
          :source-map true)
    (speak)))

(deftask prod []
  (comp
    (from-cljsjs :profile :production)

    ; Copy auxiliary files into the output directory
    ; to allow serving them with a static http server instead of
    ; serving them directly from the jar via the classpath
    (sift :to-resource #{#"themes"})
    (sift :to-resource #{#"semantic-ui.inc.css"})

    ; Call the minified file the same as in development
    ; so the inclusion into the page don't have to be changed
    (sift :move {#"^semantic-ui.min.inc.css$" "semantic-ui.css"})

    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)
    (sift :invert true :include #{#".out/"})))
