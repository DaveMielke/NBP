ipawsCommand_ping() {
   local identifier="${1}"
   local next="${2}"

   writeClientResponse "pong ${identifier}"
}

