(ns zmqguide.taskwork2
  (:require [zeromq.zmq :as zmq]))

(defn -main []
  (println "Start taskwork2")
  (let [context (zmq/zcontext)
        poller (zmq/poller context 2)
        continue (atom true)]

    (with-open [receiver (doto
                           (zmq/socket context :pull)
                           (zmq/connect "tcp://localhost:5557"))
                sender (doto
                         (zmq/socket context :push)
                         (zmq/connect "tcp://localhost:5558"))
                controller (doto
                             (zmq/socket context :sub)
                             (zmq/connect "tcp://localhost:5559")
                             (zmq/subscribe ""))]

      (zmq/register poller receiver :pollin)
      (zmq/register poller controller :pollin)

      (while @continue
        (zmq/poll poller)
        
        (when (zmq/check-poller poller 0 :pollin)
          (let [msg (zmq/receive-str receiver)
                work (Long/parseLong msg)]
            (Thread/sleep work)
            (zmq/send-str sender msg)
            (print ".")))
        
        (when (zmq/check-poller poller 1 :pollin)
          (print "STOP")
          (reset! continue false))
        
        (flush)))

    (zmq/destroy context)))
