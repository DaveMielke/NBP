ipawsSession_haveAlerts() {
   local identifier

   for identifier
   do
      local file="${identifier}.${alertFileExtension}"

      if [ ! -f "${file}" ]
      then
         writeClientResponse "removeAlert ${identifier}"
      elif ! ipawsHasAlert "${identifier}"
      then
         ipawsInsertAlert "${identifier}"
      fi
   done
}

