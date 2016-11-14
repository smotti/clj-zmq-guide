(ns zmqguide.rrworker
  (:require [zeromq.zmq :as zmq]))

(defn -main []
  (let [context (zmq/zcontext)]
    (with-open [responder (doto
                            (zmq/socket context :rep)
                            (zmq/connect "tcp://127.0.0.1:5560"))]
      (while (not (.. Thread currentThread isInterrupted))
        (let [msg (zmq/receive-str responder)]
          (println "Received request:" msg)
          (Thread/sleep 1000)
          (zmq/send-str responder "World"))))))
