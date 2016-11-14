(ns zmqguide.mtserver
  (:require [zeromq
             [zmq :as zmq]
             [device :as dev]]))

(defn worker
  [ctx]
  (println "Start worker")
  (with-open [receiver (doto
                   (zmq/socket ctx :rep)
                   (zmq/connect "inproc://workers"))]
    (while (not (.. Thread currentThread isInterrupted))
      (let [msg (zmq/receive-str receiver)]
        (println (format "Received request: [%s]." msg))
        (Thread/sleep 1000)
        (zmq/send-str receiver "World")))))

(defn -main
  []
  (println "Start mtserver")
  (let [context (zmq/zcontext)
        w (partial worker context)]
    (with-open [clients (doto
                          (zmq/socket context :router)
                          (zmq/bind "tcp://*:5555"))
                workers (doto
                          (zmq/socket context :dealer)
                          (zmq/bind "inproc://workers"))]
      (dotimes [i 5]
        (.. (Thread. w) start))
      (dev/proxy context clients workers))
    (zmq/destroy context)))
