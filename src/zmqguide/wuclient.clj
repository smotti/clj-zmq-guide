(ns zmqguide.wuclient
  (:require [zeromq.zmq :as zmq]
            [clojure.string :as str]))

(defn message->temperature [socket]
  (-> (zmq/receive-str socket)
      (str/split #" ")
      (second)
      (#(Long/parseLong %))))

(defn -main [& args]
  (println "Collecting updates from weather server...")
  (let [zipcode (or (first args) "10001")
        context (zmq/zcontext)]
    (let [subscriber (doto
                       (zmq/socket context :sub)
                       (zmq/connect "tcp://127.0.0.1:5556")
                       (zmq/subscribe zipcode))]
      (try
        (let [times 10
              temps (repeatedly times (partial message->temperature subscriber))
              avg (int (/ (apply + temps) (count temps)))]
          (printf "Average temperature for zipcode '%s' was %d\n" zipcode avg))
        (finally (.destroySocket context subscriber)
                 (.destroy context))))))
