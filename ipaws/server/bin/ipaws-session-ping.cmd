ipawsSession_ping() {
   local identifier="${1}"

   writeClientResponse "pong ${identifier}"
}

