(defproject zmqguide "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["sonatype" {:url "https://oss.sonatype.org/content/repositories/snapshots"
                             :update :always}]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.zeromq/jeromq "0.3.5"]
                 [org.zeromq/cljzmq "0.1.5-SNAPSHOT" :exclusions [org.zeromq/jzmq]]]
  :profiles {:wuserver {:main zmqguide.wuserver}
             :wuclient {:main zmqguide.wuclient}
             :taskvent {:main zmqguide.taskvent}
             :taskwork {:main zmqguide.taskwork}
             :tasksink {:main zmqguide.tasksink}
             :rrclient {:main zmqguide.rrclient}
             :rrworker {:main zmqguide.rrworker}
             :rrbroker {:main zmqguide.rrbroker}
             :msgqueue {:main zmqguide.msgqueue}
             :taskwork2 {:main zmqguide.taskwork2}
             :tasksink2 {:main zmqguide.tasksink2}
             :mtserver {:main zmqguide.mtserver}
             :mtrelay {:main zmqguide.mtrelay}}
  :aliases {"wuserver" ["with-profile" "wuserver" "run"]
            "wuclient" ["with-profile" "wuclient" "run"]
            "taskvent" ["with-profile" "taskvent" "run"]
            "taskwork" ["with-profile" "taskwork" "run"]
            "tasksink" ["with-profile" "tasksink" "run"]
            "rrclient" ["with-profile" "rrclient" "run"]
            "rrworker" ["with-profile" "rrworker" "run"]
            "rrbroker" ["with-profile" "rrbroker" "run"]
            "msgqueue" ["with-profile" "msgqueue" "run"]
            "taskwork2" ["with-profile" "taskwork2" "run"]
            "tasksink2" ["with-profile" "tasksink2" "run"]
            "mtserver" ["with-profile" "mtserver" "run"]
            "mtrelay" ["with-profile" "mtrelay" "run"]})
