(ns zmqguide.msgqueue
  (:require [zeromq
             [device :as device]
             [zmq :as zmq]]))

(defn -main []
  (let [context (zmq/zcontext)]
    (with-open [frontend (doto
                           (zmq/socket context :router)
                           (zmq/bind "tcp://*:5559"))
                backend (doto
                          (zmq/socket context :dealer)
                          (zmq/bind "tcp://*:5560"))]
      (device/proxy context frontend backend))))
