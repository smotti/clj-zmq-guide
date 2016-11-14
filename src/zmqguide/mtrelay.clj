(ns zmqguide.mtrelay
  (:require [zeromq.zmq :as zmq]))

(defn step1
  [ctx]
  (with-open [xmitter (doto
                        (zmq/socket ctx :pair)
                        (zmq/connect "inproc://step2"))]
    (println "Step 1 is ready, signaling step 2")
    (zmq/send-str xmitter "READY")))

(defn step2
  [ctx]
  (with-open [receiver (doto
                         (zmq/socket ctx :pair)
                         (zmq/bind "inproc://step2"))
              xmitter (doto
                        (zmq/socket ctx :pair)
                        (zmq/connect "inproc://step3"))]
    (let [s1 (partial step1 ctx)]
      (.. (Thread. s1) start))
    (zmq/receive-str receiver)
    (println "Step 2 is ready, signaling step 3")
    (zmq/send-str xmitter "READY")))

(defn -main []
  (let [context (zmq/zcontext)]
    (with-open [receiver (doto
                           (zmq/socket context :pair)
                           (zmq/bind "inproc://step3"))]
      (let [s2 (partial step2 context)]
        (.. (Thread. s2) start))
      (zmq/receive-str receiver)
      (println "Test successful!"))
    (zmq/destroy context)))
