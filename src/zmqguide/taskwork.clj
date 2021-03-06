(ns zmqguide.taskwork
  (:require [zeromq.zmq :as zmq]))

(defn -main
  []
  (let [context (zmq/zcontext 1)]
    (with-open [receiver (doto
                           (zmq/socket context :pull)
                           (zmq/connect "tcp://127.0.0.1:5557"))
                sender (doto
                         (zmq/socket context :push)
                         (zmq/connect "tcp://127.0.0.1:5558"))]
      (while (not (.. Thread currentThread isInterrupted))
        (let [string (zmq/receive-str receiver)]
          (println string ".")
          (Thread/sleep (Long/parseLong string))
          (zmq/send-str sender ""))))))
